package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Category
import com.pocketlearningapps.timeline.lib.requireSerializable
import kotlinx.android.synthetic.main.dialog_level_selector.*

private const val ARG_CATEGORY = "ARG_CATEGORY"

class LevelSelectorDialog : BottomSheetDialogFragment() {
    interface Listener {
        fun onLevelSelected(category: Category, level: Int)
    }

    private val category by lazy { arguments.requireSerializable<Category>(ARG_CATEGORY) }

    private lateinit var adapter: LevelSelectorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_level_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LevelSelectorAdapter()
        adapter.setCategory(category)
        adapter.clickListener = this::onLevelSelected
        category_levels.adapter = adapter

        category_levels.layoutManager = LinearLayoutManager(context)
        category_name.text = category.name
    }

    private fun onLevelSelected(level: Int) {
        dismiss()
        (requireParentFragment() as Listener).onLevelSelected(category, level)
    }

    companion object {
        fun build(category: Category) = LevelSelectorDialog().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_CATEGORY, category.copy(timeline = null, events = null))
            }
        }
    }
}
