package com.pocketlearningapps.timeline

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*


private const val RC_SIGN_IN = 100

class MainActivity : AppCompatActivity() {
    lateinit var signInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sign_in.setOnClickListener { signIn() }
        sign_out.setOnClickListener { signOut() }

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(this, signInOptions)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            displaySignedIn(account)
        } else {
            displaySignedOut()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun signIn() {
        startActivityForResult(signInClient.signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        signInClient.signOut()
            .addOnCompleteListener(this) { displaySignedOut() }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java) as GoogleSignInAccount
            displaySignedIn(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("MainActivity", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun displaySignedIn(account: GoogleSignInAccount) {
        email.text = "Signed in as ${account.email}"
        sign_in.isVisible = false
        sign_out.isVisible = true
    }

    private fun displaySignedOut() {
        email.text = "Please sign in"
        sign_in.isVisible = true
        sign_out.isVisible = false
    }
}
