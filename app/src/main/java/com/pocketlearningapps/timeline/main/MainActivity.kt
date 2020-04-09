package com.pocketlearningapps.timeline.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.auth.AuthResultContract
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.network.RetrofitServiceFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val signInAdapter by lazy { GoogleSignInAdapter(application) }

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            signInAdapter
        )
    }

    private val signInLauncher by lazy {
        prepareCall(
            AuthResultContract(signInAdapter),
            viewModel::onAuthResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RetrofitServiceFactory.initialize(application)

        viewModel.signIn.observe(this, Observer { signInLauncher.launch(Unit) })
        viewModel.viewState.observe(this, Observer { updateViewState(it) })

        sign_in.setOnClickListener { viewModel.signInClicked() }
        sign_out.setOnClickListener { viewModel.signOutClicked() }
    }

    private fun updateViewState(viewState: MainViewState) {
        email.text = viewState.email
        name.text = viewState.name
        sign_in.isVisible = viewState.showSignInButton
        sign_out.isVisible = viewState.showSignOutButton
    }
}
