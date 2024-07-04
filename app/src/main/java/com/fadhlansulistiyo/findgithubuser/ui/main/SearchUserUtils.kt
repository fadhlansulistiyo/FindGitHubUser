package com.fadhlansulistiyo.findgithubuser.ui.main

import android.app.Activity
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.fadhlansulistiyo.findgithubuser.R
import com.fadhlansulistiyo.findgithubuser.ui.favorite.FavoriteActivity
import com.fadhlansulistiyo.findgithubuser.ui.setting.SettingActivity
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class SearchUserUtils {
    fun setUpSearchView(
        activity: AppCompatActivity,
        searchBar: SearchBar,
        searchView: SearchView,
        loadSearchUser: (String) -> Unit
    ) {
        searchView.inflateMenu(R.menu.option_menu)
        searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = searchView.text.toString()
            searchBar.setText(query)
            searchView.hide()

            loadSearchUser(query)
            false
        }
        val onBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                searchView.hide()
            }
        }
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
        searchView.addTransitionListener { _, _, newState ->
            onBackPressedCallback.isEnabled = newState == SearchView.TransitionState.SHOWN
        }
    }

    fun setUpSearchBar(activity: Activity, searchBar: SearchBar) {
        searchBar.inflateMenu(R.menu.option_menu)
        searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_favorite -> {
                    val intent = Intent(activity, FavoriteActivity::class.java)
                    activity.startActivity(intent)
                }
                R.id.action_settings -> {
                    val intent = Intent(activity, SettingActivity::class.java)
                    activity.startActivity(intent)
                }
            }
            true
        }
    }
}