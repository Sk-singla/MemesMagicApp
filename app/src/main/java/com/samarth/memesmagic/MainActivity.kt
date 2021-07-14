package com.samarth.memesmagic

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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmFollowerAddedMessage
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmMessage
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmMessageData
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_SEND_MESSAGE
import com.samarth.memesmagic.ui.MainNavGraph
import com.samarth.memesmagic.ui.screens.RegisterScreen.RegisterScreenViewModel
import com.samarth.memesmagic.ui.theme.MemesMagicTheme
import com.samarth.memesmagic.util.Constants.FCM_TYPE_FOLLOWER_ADDED
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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



    lateinit var receiver: BroadcastReceiver



    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if(it.data != null) {
            Toast.makeText(this, "Data is not null", Toast.LENGTH_SHORT).show()
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Toast.makeText(this, "${task}", Toast.LENGTH_SHORT).show()
            Log.d("Task","$task")
            handleSignInResult(task)
        } else {
            Toast.makeText(this, "Data is Null", Toast.LENGTH_SHORT).show()
        }
    }
    
    private var showAlertBox = mutableStateOf(false)
    private var followerMessage = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemesMagicTheme {
                Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
                    MainNavGraph(
                        startActivity = { intent ->
                            startActivity(intent)
                        },
                        startActivityForResult = { str, done ->
                            imagePicker.launch(str)
                            Log.d("MyLog", "done -> ${imageUri.value}")
                            lifecycleScope.launchWhenCreated {
                                imageUri.collect {
                                    if (it != null) {
                                        done(it)
                                    }
                                }
                            }
                        },
                        updateOrRequestPermissions = {
                            updateOrRequestPermissions()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    if(showAlertBox.value) {
                        AlertDialog(onDismissRequest = {
                            showAlertBox.value = false
                        },
                            buttons = {

                            },
                            title = {
                                Text(text = "New Follower!")
                            },
                            text = {
                                Text(text = followerMessage)
                            }

                        )
                    }
                    
                    
                }
            }
        }
        
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val strMessage = intent!!.getStringExtra("message")
                Log.d("fcm message",strMessage ?: "NULL")
                val messageObject = JsonParser.parseString(strMessage).asJsonObject
                val type = messageObject.get("type").asString
                Log.d("fcm type","$type ,$FCM_TYPE_FOLLOWER_ADDED, equal? = ${type == FCM_TYPE_FOLLOWER_ADDED}")
                val message = when(type){
                    FCM_TYPE_FOLLOWER_ADDED -> Gson().fromJson(strMessage, FcmFollowerAddedMessage::class.java)
                    else -> Gson().fromJson(strMessage, FcmMessageData::class.java)
                }
                when(message) {
                    is FcmFollowerAddedMessage -> {
                        followerMessage = message.message
                        showAlertBox.value = true
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Not fcm follower message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
//        configureGSI()
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





    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account:GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            val accountLast = GoogleSignIn.getLastSignedInAccount(this)
            updateUI(accountLast)
        }catch (e:Exception){
            Timber.d(e)
            updateUI(null)
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


    fun configureGSI(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(BuildConfig.API_KEY)
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(
            this,
            gso
        )
        val account = GoogleSignIn.getLastSignedInAccount(
            this
        )
        if(account != null)
            updateUI(account)
        else
            signIn()
    }


    fun signIn(){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult.launch(signInIntent)
    }


    private fun updateOrRequestPermissions():Boolean{
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