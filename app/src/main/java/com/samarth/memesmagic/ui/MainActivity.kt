package com.samarth.memesmagic.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.samarth.memesmagic.BuildConfig
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.request.LoginRequest
import com.samarth.memesmagic.data.remote.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmFollowerAddedMessage
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmMessageData
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_NEW_FOLLOWER
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_SEND_MESSAGE
import com.samarth.memesmagic.ui.screens.chat.ChatViewModel
import com.samarth.memesmagic.ui.theme.MemesMagicTheme
import com.samarth.memesmagic.util.Constants.FCM_TYPE_FOLLOWER_ADDED
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN
import com.samarth.memesmagic.util.TokenHandler
import com.samarth.memesmagic.util.navigateWithPop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity(), LifecycleObserver {
    private lateinit var mGoogleSignInClient:GoogleSignInClient

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private val permissionsLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
        writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted
    }

    var imageUri = MutableStateFlow<Uri?>(null)
    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        imageUri.value = uri
        Log.d("MyLog","came -> ${uri}")
    }

    private val googleSignInIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.data != null) {
            Toast.makeText(this, "Data is not null", Toast.LENGTH_SHORT).show()
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Toast.makeText(this, "${task}, Sucess: ${task.isSuccessful}", Toast.LENGTH_SHORT).show()
            Log.d("Task","Success: ${task.isSuccessful}")
            handleSignInResult(task)
        } else {
            Toast.makeText(this, "Data is Null", Toast.LENGTH_SHORT).show()
        }
    }
    lateinit var receiver: BroadcastReceiver
    private lateinit var navController:NavHostController

    @Inject
    lateinit var memeRepo: MemeRepo
    private val chatViewModel:ChatViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemesMagicTheme {
                navController = rememberNavController()
                MainNavGraph(
                    startActivity = { intent ->
                        startActivity(intent)
                    },
                    startActivityForResult = { str, done ->
                        imagePicker.launch(str)
                        lifecycleScope.launchWhenCreated {
                            imageUri.collect {
                                if (it != null) {
                                    done(it)
                                }
                            }
                        }
                    },
                    updateOrRequestPermissions = {
                        updateOrRequestStoragePermissions()
                    },
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    chatViewModel = chatViewModel,
                    onSignUpWithGoogle = {
                        configureGSI()
                        signIn()
                    }
                )
            }
        }

        notificationIntentWork(intent)


        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val strMessage = intent!!.getStringExtra("message")
                val messageObject = JsonParser.parseString(strMessage).asJsonObject
                val message = when(messageObject.get("type").asString){
                    FCM_TYPE_FOLLOWER_ADDED -> Gson().fromJson(strMessage, FcmFollowerAddedMessage::class.java)
                    else -> Gson().fromJson(strMessage, FcmMessageData::class.java)
                }
                when(message) {
                    is FcmFollowerAddedMessage -> {
                        Snackbar.make(
                            this@MainActivity,
                            View(this@MainActivity),
                            message.message,
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.follow_back) {
                            lifecycleScope.launch {
                                memeRepo.followUser(
                                    message.followerInfo.email
                                )
                            }
                        }.show()
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Not fcm follower message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        chatViewModel.listenToConnectionEvent()
        chatViewModel.observeWebSocketBaseModelEvents()
        chatViewModel.observeConnectionEvents()
    }

    private fun notificationIntentWork(intent: Intent?) {

        Log.d("fcm_intent",intent?.action ?: "NULL")
        Log.d("fcm Intent",intent?.getStringExtra("intentOfIntent") ?: "intent is null")
        Log.d("fcm any Intent",intent?.extras?.get("intentOfIntent")?.toString() ?: "NULL")
        when(intent?.getStringExtra("intentOfIntent")) {
            INTENT_ACTION_NEW_FOLLOWER -> {
                Timber.d("Intent reached!")
                navController.navigate(
                    "$ANOTHER_USER_PROFILE_SCREEN/${intent.getStringExtra("followerEmail") ?: ""}"
                )
            }
            else -> Unit
        }
    }

    override fun onNewIntent(intent: Intent?) {
        notificationIntentWork(intent)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(INTENT_ACTION_SEND_MESSAGE)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,filter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppInBackground(){
        chatViewModel.disconnect()
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        lifecycleScope.launch {
            try {
                task?.result?.let {
                    Log.d("TASK","Result entered, ${it.email}")


                    val result = memeRepo.registerUser(
                        userRegisterRequest = RegisterUserRequest(
                            name = it.displayName ?: "",
                            email = it.email ?: "",
                            password = it.id ?: UUID.randomUUID().toString(),
                            profilePic = it.photoUrl?.toString()
                        )
                    )

                    if(result.data != null) {
                        TokenHandler.saveJwtToken(
                            this@MainActivity,
                            result.data,
                            it.email ?: ""
                        )
                        navController.popBackStack()
                        navigateWithPop(navController, Screens.HOME_SCREEN)
                    } else {
                        val loginResult = memeRepo.loginUser(
                            loginRequest = LoginRequest(
                                email = it.email ?: "",
                                password = it.id ?: ""
                            )
                        )
                        if(loginResult.data != null) {
                            TokenHandler.saveJwtToken(
                                this@MainActivity,
                                loginResult.data,
                                it.email ?: ""
                            )
                            navController.popBackStack()
                            navigateWithPop(navController, Screens.HOME_SCREEN)
                        }

                    }
                    updateUI(it)
                }
            }catch (e:Exception){
                Timber.d(e)
                e.printStackTrace()
                Log.d("EXC",e.message ?: "Exception Occrued!")
                updateUI(null)
            }
        }
    }

    private fun updateUI(account:GoogleSignInAccount?){
        if(account != null){
            Timber.d("${account.email}, ${account.displayName}")
            Toast.makeText(this, "${account.email}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "NULL NULL NULL", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }


    fun configureGSI(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.API_KEY)
            .requestEmail()
            .requestProfile()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(
            this,
            gso
        )
    }


    fun signIn(){
        mGoogleSignInClient.signInIntent.also {
            googleSignInIntent.launch(it)
        }
    }


    private fun updateOrRequestStoragePermissions():Boolean{
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionsToRequest = mutableListOf<String>()
        if(!writePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(!readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
        return writePermissionGranted && readPermissionGranted
    }




}