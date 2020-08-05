package com.pocketlearningapps.timeline.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Category
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_quiz_selector.*
import retrofit2.HttpException

private const val REQUEST_CODE = 0x10

class QuizSelectorFragment : Fragment(R.layout.fragment_quiz_selector), HasContainer, LevelSelectorDialog.Listener {
    override val container by lazy { (activity?.application as HasContainer).container }

    private val service: RetrofitService by inject()
    private lateinit var adapter: QuizOptionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = QuizOptionsAdapter()
        timelines.adapter = adapter
        timelines.layoutManager = LinearLayoutManager(context)
        adapter.onQuizOptionClicked = this::onQuizOptionClicked

        refreshData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            refreshData()
        }
    }

    override fun onLevelSelected(category: Category, level: Int) {
        val intent = Intent(requireContext(), QuizActivity::class.java).apply {
            putExtra("CATEGORY_ID", category.id)
            putExtra("LEVEL", level)
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun onQuizOptionClicked(category: Category, level: Int) {
        LevelSelectorDialog.build(category)
            .show(childFragmentManager, "LEVEL_SELECTOR")
    }

    private fun refreshData() {
        lifecycleScope.launchWhenCreated {
            try {
                val timelines = service.timelines()
                adapter.setData(timelines)
            } catch (e: HttpException) {
                Log.d("HttpError", "Code: " + e.code())
            }
        }
    }
}
