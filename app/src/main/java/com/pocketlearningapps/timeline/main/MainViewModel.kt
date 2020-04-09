package com.pocketlearningapps.timeline.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.pocketlearningapps.timeline.auth.AuthResult
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.lib.SingleLiveAction
import com.pocketlearningapps.timeline.network.RetrofitService
import com.pocketlearningapps.timeline.network.RetrofitServiceFactory
import com.pocketlearningapps.timeline.network.UserResponse
import com.pocketlearningapps.timeline.network.ValidateTokenRequest
import kotlinx.coroutines.launch
import java.lang.Exception

data class MainViewState(
    val name: String?,
    val email: String?,
    val showSignInButton: Boolean,
    val showSignOutButton: Boolean
)

class MainViewModel(
    private val signInAdapter: GoogleSignInAdapter,
    private val service: RetrofitService
) : ViewModel() {
    val viewState = MutableLiveData<MainViewState>()
    val signIn = SingleLiveAction()

    init {
        updateUser(null)
    }

    fun signInClicked() {
        signIn.post()
    }

    fun signOutClicked() {
        signInAdapter.signOut { updateUser(null) }
    }

    fun onAuthResult(result: AuthResult) = when (result) {
        is AuthResult.Success -> validateAccount(result.account)
        is AuthResult.NotSignedIn -> updateUser(null)
    }

    private fun validateAccount(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                val result = service.validateToken(ValidateTokenRequest(account.idToken))
                val user = service.profile()
                Log.d(MainViewModel::class.java.simpleName, "result: " + result.toString())
                updateUser(user)
            } catch (e: Exception) {
                e.printStackTrace();
            }

        }
    }

    private fun updateUser(user: UserResponse?) {
        val viewState = if (user != null) {
            MainViewState(
                email = user.email,
                name = user.name,
                showSignInButton = false,
                showSignOutButton = true
            )
        } else {
            MainViewState(
                email = null,
                name = null,
                showSignInButton = true,
                showSignOutButton = false
            )
        }
        this.viewState.postValue(viewState)
    }
}
