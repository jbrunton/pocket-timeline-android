package com.pocketlearningapps.timeline.ui.timelines

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.facebook.react.PackageList
import com.facebook.react.ReactFragment
import com.facebook.react.ReactNativeHost
import com.pocketlearningapps.timeline.BuildConfig
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.fragment_timelines.*


class TimelinesFragment : Fragment(R.layout.fragment_timelines) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }
}

class TimelinesReactFragment : ReactFragment() {

    init {
        val args = Bundle()
        args.putString("arg_component_name", "ReactTest")
        args.putBundle("arg_launch_options", Bundle())
        arguments = args
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return object : ReactNativeHost(requireActivity().application) {
            override fun getPackages() = PackageList(this).packages
            override fun getUseDeveloperSupport() = BuildConfig.DEBUG
            override fun getJSMainModuleName() = "index"
        }
    }
}
