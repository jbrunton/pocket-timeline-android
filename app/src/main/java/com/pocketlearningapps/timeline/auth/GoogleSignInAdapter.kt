package com.pocketlearningapps.timeline.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSignInAdapter(val context: Context) {
    val signInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("952635847674-ocr6762iqhjkvtb988fclnfs4trr6qqr.apps.googleusercontent.com") // web
            //.requestServerAuthCode("952635847674-jm6dkvorojgam9pg3pk17da7jtcikhsj.apps.googleusercontent.com") // android
            //.requestIdToken("952635847674-ocr6762iqhjkvtb988fclnfs4trr6qqr.apps.googleusercontent.com")
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
