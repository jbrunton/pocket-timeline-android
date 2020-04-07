package com.pocketlearningapps.timeline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.pocketlearningapps.timeline.auth.AuthResult
import com.pocketlearningapps.timeline.auth.AuthResultContract
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val signInAdapter by lazy {
        GoogleSignInAdapter(
            this
        )
    }

    private val signIn = prepareCall(
        AuthResultContract(
            signInAdapter
        )
    ) { result ->
        when (result) {
            is AuthResult.Success -> displaySignedIn(result.account)
            is AuthResult.NotSignedIn -> displaySignedOut()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sign_in.setOnClickListener { signIn.launch(Unit) }
        sign_out.setOnClickListener { signOut() }

        val account = signInAdapter.lastSignedInAccount
        if (account != null) {
            displaySignedIn(account)
        } else {
            displaySignedOut()
        }
    }

    private fun signOut() {
        signInAdapter.signOut(this::displaySignedOut)
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
