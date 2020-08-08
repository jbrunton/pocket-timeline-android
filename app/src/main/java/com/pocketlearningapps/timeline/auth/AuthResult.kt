package com.pocketlearningapps.timeline.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class AuthResult {
    data class Success(val account: GoogleSignInAccount) : AuthResult()
    object NotSignedIn : AuthResult()
}