package com.pocketlearningapps.timeline.ui.main

import com.pocketlearningapps.timeline.network.UserResponse

class MainViewStateFactory {
    fun viewState(user: UserResponse?): MainViewState {
        return  if (user != null) {
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
    }
}