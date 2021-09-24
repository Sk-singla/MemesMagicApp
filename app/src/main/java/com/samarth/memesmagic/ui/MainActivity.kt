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
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.samarth.memesmagic.BuildConfig
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.remote.request.LoginRequest
import com.samarth.memesmagic.data.remote.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom
import com.samarth.memesmagic.notification.models.BaseNotification
import com.samarth.memesmagic.notification.models.BaseNotification.Companion.NOTIFICATION_TYPE_NEW_FOLLOWER
import com.samarth.memesmagic.notification.models.NewFollowerNotification
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_CHAT_MESSAGE
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_FCM_MESSAGE
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_NEW_FOLLOWER
import com.samarth.memesmagic.ui.screens.chat.ChatViewModel
import com.samarth.memesmagic.ui.theme.MemesMagicTheme
import com.samarth.memesmagic.util.*
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.CHAT_ROOMS_LIST_SCREEN
import com.samarth.memesmagic.util.Screens.CHAT_ROOM_SCREEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        lifecycleScope.launch{
            imageUri.emit(uri)
        }
        Log.d("MyLog","came -> ${uri}")
    }

    @Inject
    lateinit var gson:Gson

    private val googleSignInIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Log.d("Task","Success: ${task.isSuccessful}")
            handleSignInResult(task)
        }
    }
    lateinit var receiver: BroadcastReceiver
    private lateinit var navController:NavHostController

    @Inject
    lateinit var memeRepo: MemeRepo
    private val chatViewModel:ChatViewModel by viewModels()

    @ExperimentalMaterialApi
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
                    },
                    navigateWithNotification = {
                        notificationIntentWork(intent,it)
                    },
                )
            }
        }


        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val strMessage = intent!!.getStringExtra("message")
                val messageObject = JsonParser.parseString(strMessage).asJsonObject
                val type = when(messageObject.get("type").asString){
                    NOTIFICATION_TYPE_NEW_FOLLOWER -> NewFollowerNotification::class.java
                    else -> BaseNotification::class.java
                }

//                when(val message = gson.fromJson(strMessage,type)) {
//                    is NewFollowerNotification -> {
//                        Snackbar.make(
//                            this@MainActivity,
//                            View(this@MainActivity),
//                            message.message,
//                            Snackbar.LENGTH_LONG
//                        ).setAction(R.string.follow_back) {
//                            lifecycleScope.launch {
//                                memeRepo.followUser(
//                                    message.followerInfo.email
//                                )
//                            }
//                        }.show()
//                    }
//                    else -> {
//                        Toast.makeText(this@MainActivity, "Not fcm follower message", Toast.LENGTH_SHORT).show()
//                    }
//                }
            }
        }

        chatViewModel.listenToConnectionEvent()
        chatViewModel.observeWebSocketBaseModelEvents()
        chatViewModel.observeConnectionEvents()
    }

    private fun notificationIntentWork(intent: Intent?,mNavController: NavController) {


        Log.d("fcm_intent",intent?.action ?: "NULL")
        val notificationId = intent?.getStringExtra("notificationId")
        notificationId?.let { nId ->
            lifecycleScope.launch {
                memeRepo.seenNotification(nId)
            }
        }
        when(intent?.action) {
            INTENT_ACTION_NEW_FOLLOWER -> {
                val anotherUserEmailFromNotification = intent.getStringExtra("follower")
                mNavController.navigate("${ANOTHER_USER_PROFILE_SCREEN}/$anotherUserEmailFromNotification")
            }
            INTENT_ACTION_CHAT_MESSAGE -> {
                mNavController.navigate(CHAT_ROOMS_LIST_SCREEN)
                val userInfoText = intent.getStringExtra("msgSenderUserInfo")
                val userInfo = gson.fromJson(userInfoText,UserInfo::class.java)

                ChatUtils.currentChatRoom = PrivateChatRoom(
                    userEmail = userInfo.email,
                    name = userInfo.name,
                    profilePic = userInfo.profilePic
                )
                Log.d("notification","Sender: ${userInfo.email}, ${userInfo.name}")
                mNavController.navigate(CHAT_ROOM_SCREEN)
            }
            else -> null
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(INTENT_ACTION_FCM_MESSAGE)
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
                if(task?.result != null){
                    val taskResult = task.result
                    val result = memeRepo.registerUser(
                        userRegisterRequest = RegisterUserRequest(
                            name = taskResult.displayName ?: "",
                            email = taskResult.email ?: "",
                            password = taskResult.id ?: UUID.randomUUID().toString(),
                            profilePic = taskResult.photoUrl?.toString()
                        )
                    )

                    updateUi(result, taskResult.email ?: "")
                } else {
                    throw Exception("Task Result is NULL!")
                }
            }catch (e:Exception){
                Timber.d(e)
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Some Problem Occurred!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun updateUi(
        result: Resource<String>,
        email:String
    ) {
        if (result.data != null) {
            TokenHandler.saveJwtToken(
                this@MainActivity,
                result.data,
                email
            )
            navController.popBackStack()
            navigateWithPop(navController, Screens.HOME_SCREEN)
        } else {
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
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