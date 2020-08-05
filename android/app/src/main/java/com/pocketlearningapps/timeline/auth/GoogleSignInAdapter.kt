package com.pocketlearningapps.timeline.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.auth.session.SessionManager

class GoogleSignInAdapter(
    private val context: Context,
    private val sessionManager: SessionManager
) {
    private val signInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(context.getString(R.string.server_client_id))
            .build()
    }

    private val signInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            context,
            signInOptions
        )
    }

    val signInIntent get() = signInClient.signInIntent

    fun signOut(onComplete: () -> Unit) {
        signInClient.signOut().addOnCompleteListener {
            sessionManager.clear()
            onComplete()
        }
    }

    val lastSignedInAccount get() = GoogleSignIn.getLastSignedInAccount(
        context
    )
}
