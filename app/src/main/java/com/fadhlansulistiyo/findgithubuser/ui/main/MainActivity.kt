package com.fadhlansulistiyo.findgithubuser.ui.main

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhlansulistiyo.findgithubuser.R
import com.fadhlansulistiyo.findgithubuser.data.remote.response.UserItems
import com.fadhlansulistiyo.findgithubuser.data.repo.ResultState
import com.fadhlansulistiyo.findgithubuser.databinding.ActivityMainBinding
import com.fadhlansulistiyo.findgithubuser.ui.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var searchState: SearchState = SearchState.INITIAL

    companion object {
        const val MESSAGE_API_KEY = "Have you entered the api key in BuildConfig?"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val setUpSearchUser = SearchUserUtils()
        setUpSearchUser.setUpSearchBar(this, binding.searchBar)
        setUpSearchUser.setUpSearchView(this, binding.searchBar, binding.searchView) {
            viewModel.getSearchUser(it)
        }

        setUpTheme()
        observeSearchResult()
        updateUI()
    }

    private fun observeSearchResult() {
        viewModel.searchResults.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    updateState(SearchState.RESULTS)
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)

                    val userData = result.data.items
                    if (userData.isEmpty()) {
                        updateState(SearchState.NOT_FOUND)
                    } else {
                        updateState(SearchState.RESULTS)
                        setListData(userData)
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    updateState(SearchState.NOT_FOUND)
                    showSnackbar(this, MESSAGE_API_KEY)
                }
            }
        }
    }

    private fun setListData(user: List<UserItems>) {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        val adapter = ListUserAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(user)
    }

    private fun updateState(state: SearchState) {
        searchState = state
        updateUI()
    }

    private fun updateUI() {
        when (searchState) {
            SearchState.INITIAL -> {
                binding.apply {
                    initialIcon.visibility = View.VISIBLE
                    notFoundIcon.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
            }

            SearchState.RESULTS -> {
                binding.apply {
                    initialIcon.visibility = View.GONE
                    notFoundIcon.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }

            SearchState.NOT_FOUND -> {
                binding.apply {
                    initialIcon.visibility = View.GONE
                    notFoundIcon.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbar(activity: Activity, message: String) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun updateThemeIcons(isDarkModeActive: Boolean) {
        val initialIcon = binding.initialIcon
        val notFoundIcon = binding.notFoundIcon

        if (isDarkModeActive) {
            initialIcon.setImageResource(R.drawable.explore_user_dark_theme)
            notFoundIcon.setImageResource(R.drawable.user_not_found_dark)
        } else {
            initialIcon.setImageResource(R.drawable.explore_user_light)
            notFoundIcon.setImageResource(R.drawable.user_not_found_light)
        }
    }

    private fun setUpTheme() {
        viewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            setTheme(isDarkModeActive)
            updateThemeIcons(isDarkModeActive)
        }
    }

    private fun setTheme(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}