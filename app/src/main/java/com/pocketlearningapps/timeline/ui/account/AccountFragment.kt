package com.pocketlearningapps.timeline.ui.account

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.inject.injectViewModel
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.auth.AuthResultContract
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.ui.main.MainViewState
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment(R.layout.fragment_account), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }

    private val signInAdapter: GoogleSignInAdapter by inject()
    private val viewModel: AccountViewModel by injectViewModel()

    private val signInLauncher by lazy {
        prepareCall(
            AuthResultContract(signInAdapter),
            viewModel::onAuthResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.signIn.observe(this, Observer { signInLauncher.launch(Unit) })
        viewModel.viewState.observe(this, Observer { updateViewState(it) })
        viewModel.showErrorDialog.observe(this, Observer { showErrorDialog(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

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
        AlertDialog.Builder(requireActivity())
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
