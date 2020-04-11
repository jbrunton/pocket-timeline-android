package com.pocketlearningapps.timeline.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.pocketlearningapps.timeline.auth.AuthResult
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.auth.session.SessionManager
import com.pocketlearningapps.timeline.lib.SingleLiveAction
import com.pocketlearningapps.timeline.lib.SingleLiveEvent
import com.pocketlearningapps.timeline.network.RetrofitService
import com.pocketlearningapps.timeline.network.UserResponse
import com.pocketlearningapps.timeline.network.ValidateTokenRequest
import com.pocketlearningapps.timeline.ui.main.MainViewState
import com.pocketlearningapps.timeline.ui.main.MainViewStateFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class AccountViewModel(
    private val signInAdapter: GoogleSignInAdapter,
    private val service: RetrofitService,
    private val sessionManager: SessionManager
) : ViewModel() {
    val viewState = MutableLiveData<MainViewState>()
    val signIn = SingleLiveAction()
    val showErrorDialog = SingleLiveEvent<String>()

    private val viewStateFactory =
        MainViewStateFactory()

    init {
        refreshUser()
    }

    fun signInClicked() {
        signIn.post()
    }

    fun signOutClicked() {
        signInAdapter.signOut {
            refreshUser()
        }
    }

    fun onAuthResult(result: AuthResult) = when (result) {
        is AuthResult.Success -> validateAccount(result.account)
        is AuthResult.NotSignedIn -> updateUser(null)
    }

    fun refreshUser() {
        viewModelScope.launch {
            if (sessionManager.hasSession) {
                try {
                    val user = service.profile()
                    updateUser(user)
                } catch (e: HttpException) {
                    updateUser(null)
                }
            } else {
                updateUser(null)
            }
        }
    }

    private fun validateAccount(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                service.validateToken(ValidateTokenRequest(account.idToken))
                refreshUser()
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorDialog.postValue("Unable to verify token")
            }
        }
    }

    private fun updateUser(user: UserResponse?) {
        this.viewState.postValue(viewStateFactory.viewState(user))
    }
}
