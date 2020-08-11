package com.pocketlearningapps.timeline.ui.timelines

import android.os.Bundle
import com.facebook.react.PackageList
import com.facebook.react.ReactFragment
import com.facebook.react.ReactNativeHost
import com.pocketlearningapps.timeline.BuildConfig


//class TimelinesFragment : Fragment(R.layout.fragment_timelines), HasContainer {
//    override val container by lazy { (activity?.application as HasContainer).container }
//
//    private val service: RetrofitService by inject()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//
//        val adapter = TimelinesAdapter()
//        timelines.adapter = adapter
//        timelines.layoutManager = LinearLayoutManager(activity)
//        adapter.onTimelineClicked = this::onTimelineClicked
//
//        lifecycleScope.launchWhenStarted {
//            try {
//                val timelines = service.timelines()
//                adapter.setData(timelines)
//            } catch (e: HttpException) {
//                Log.d("HttpError", "Code: " + e.code())
//            }
//        }
//    }
//
//    private fun onTimelineClicked(timeline: Timeline) {
//        val intent = Intent(requireContext(), TimelineActivity::class.java).apply {
//            putExtra("TIMELINE_ID", timeline.id)
//        }
//        requireContext().startActivity(intent)
//    }
//}

class TimelinesFragment : ReactFragment() {
    override fun getReactNativeHost(): ReactNativeHost {
        return object : ReactNativeHost(requireActivity().application) {
            override fun getPackages() = PackageList(this).packages
            override fun getUseDeveloperSupport() = BuildConfig.DEBUG
            override fun getJSMainModuleName() = "index"
        }
    }

    companion object {
        fun newInstance(
            componentName: String,
            launchOptions: Bundle
        ): ReactFragment {
            val fragment = TimelinesFragment()
            val args = Bundle()
            args.putString("arg_component_name", componentName)
            args.putBundle("arg_launch_options", launchOptions)
            fragment.arguments = args
            return fragment
        }
    }

//    override fun createReactActivityDelegate(): ReactActivityDelegate {
//        return object : ReactActivityDelegate(this, mainComponentName) {
//            override fun getReactInstanceManager(): ReactInstanceManager {
//                return ReactInstanceManager.builder()
//                    .setApplication(application)
//                    .setCurrentActivity(this@MyReactActivity)
//                    .setBundleAssetName("index.android.bundle")
//                    .addPackage(MainReactPackage())
//                    .setUseDeveloperSupport(BuildConfig.DEBUG)
//                    .setInitialLifecycleState(LifecycleState.RESUMED)
//                    .build()
//            }
//
//            override fun getReactNativeHost(): ReactNativeHost {
//                return object : ReactNativeHost(application) {
//                    override fun getPackages() = PackageList(this).packages
//
//                    override fun getUseDeveloperSupport() = BuildConfig.DEBUG
//
//                    override fun getJSMainModuleName() = "index"
//                }
//            }
//        }
//    }
}
