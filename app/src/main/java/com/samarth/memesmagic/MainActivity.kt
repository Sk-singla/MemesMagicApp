package com.samarth.memesmagic

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.samarth.memesmagic.ui.MainNavGraph
import com.samarth.memesmagic.ui.Screens.RegisterScreen.RegisterScreenViewModel
import com.samarth.memesmagic.ui.theme.MemesMagicTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var mGoogleSignInClient:GoogleSignInClient
    private val mRegisterScreenViewModel:RegisterScreenViewModel by viewModels()

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemesMagicTheme {
                MainNavGraph()
            }
        }

//        configureGSI()
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





}