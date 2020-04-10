package com.pocketlearningapps.timeline.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.inject.injectViewModel
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.auth.AuthResultContract
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), HasContainer {
    override val container by lazy { (application as HasContainer).container }

    private val signInAdapter: GoogleSignInAdapter by inject()
    private val viewModel: MainViewModel by injectViewModel()

    private val signInLauncher by lazy {
        prepareCall(
            AuthResultContract(signInAdapter),
            viewModel::onAuthResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.signIn.observe(this, Observer { signInLauncher.launch(Unit) })
        viewModel.viewState.observe(this, Observer { updateViewState(it) })
        viewModel.showErrorDialog.observe(this, Observer { showErrorDialog(it) })

        sign_in.setOnClickListener { viewModel.signInClicked() }
        sign_out.setOnClickListener { viewModel.signOutClicked() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUser()
    }

    private fun updateViewState(viewState: MainViewState) {
        email.text = viewState.email
        name.text = viewState.name
        sign_in.isVisible = viewState.showSignInButton
        sign_out.isVisible = viewState.showSignOutButton
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
