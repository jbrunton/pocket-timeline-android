package com.pocketlearningapps.timeline.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.ui.account.AccountFragment
import com.pocketlearningapps.timeline.ui.quiz.QuizSelectorFragment
import com.pocketlearningapps.timeline.ui.timelines.TimelinesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main),
    BottomNavigationView.OnNavigationItemSelectedListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation.setOnNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            showFragment(QuizSelectorFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_quiz -> {
                showFragment(QuizSelectorFragment())
                true
            }
            R.id.navigation_timelines -> {
                showFragment(TimelinesFragment())
                true
            }
            R.id.navigation_account -> {
                showFragment(AccountFragment())
                true
            }
            else -> false
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .commit()
    }
}
