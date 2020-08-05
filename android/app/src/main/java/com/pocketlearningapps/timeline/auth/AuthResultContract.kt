package com.pocketlearningapps.timeline.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException

class AuthResultContract(private val adapter: GoogleSignInAdapter)
    : ActivityResultContract<Unit, AuthResult>()
{
    override fun createIntent(context: Context, input: Unit?): Intent {
        return adapter.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AuthResult {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        return try {
            val account = task.getResult(ApiException::class.java) as GoogleSignInAccount
            AuthResult.Success(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("MainActivity", "signInResult:failed code=" + GoogleSignInStatusCodes.getStatusCodeString(e.statusCode))
            AuthResult.NotSignedIn
        }
    }
}
