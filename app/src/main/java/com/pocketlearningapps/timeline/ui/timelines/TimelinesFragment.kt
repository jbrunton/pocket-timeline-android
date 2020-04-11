package com.pocketlearningapps.timeline.ui.timelines

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.fragment_timelines.*

class TimelinesFragment : Fragment(R.layout.fragment_timelines) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        show_timeline.setOnClickListener {
            startActivity(Intent(activity, TimelineActivity::class.java))
        }
    }
}
