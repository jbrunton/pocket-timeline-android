package com.pocketlearningapps.timeline.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.pocketlearningapps.timeline.auth.AuthResult
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.lib.SingleLiveAction

data class MainViewState(
    val email: String?,
    val showSignInButton: Boolean,
    val showSignOutButton: Boolean
)

class MainViewModel(val signInAdapter: GoogleSignInAdapter) : ViewModel() {
    val viewState = MutableLiveData<MainViewState>()
    val signIn = SingleLiveAction()

    init {
        updateAccount(signInAdapter.lastSignedInAccount)
    }

    fun signInClicked() {
        signIn.post()
    }

    fun signOutClicked() {
        signInAdapter.signOut { updateAccount(null) }
    }

    fun onAuthResult(result: AuthResult) = when (result) {
        is AuthResult.Success -> updateAccount(result.account)
        is AuthResult.NotSignedIn -> updateAccount(null)
    }

    private fun updateAccount(account: GoogleSignInAccount?) {
        val viewState = if (account != null) {
            MainViewState(
                email = account.email,
                showSignInButton = false,
                showSignOutButton = true
            )
        } else {
            MainViewState(
                email = null,
                showSignInButton = true,
                showSignOutButton = false
            )
        }
        this.viewState.postValue(viewState)
    }
}
