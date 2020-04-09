package com.pocketlearningapps.timeline.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.pocketlearningapps.timeline.R

class GoogleSignInAdapter(val context: Context) {
    val signInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(context.getString(R.string.server_client_id))
            .build()
    }

    val signInClient by lazy {
        GoogleSignIn.getClient(
            context,
            signInOptions
        )
    }

    fun signOut(onComplete: () -> Unit) {
        signInClient.signOut().addOnCompleteListener { onComplete() }
    }

    val lastSignedInAccount get() = GoogleSignIn.getLastSignedInAccount(
        context
    )
}
