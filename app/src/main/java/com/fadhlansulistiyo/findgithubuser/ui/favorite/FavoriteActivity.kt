package com.fadhlansulistiyo.findgithubuser.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhlansulistiyo.findgithubuser.R
import com.fadhlansulistiyo.findgithubuser.data.remote.response.UserItems
import com.fadhlansulistiyo.findgithubuser.databinding.ActivityFavoriteBinding
import com.fadhlansulistiyo.findgithubuser.ui.ViewModelFactory
import com.fadhlansulistiyo.findgithubuser.ui.main.ListUserAdapter

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // appBar
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextAppearance(
            this@FavoriteActivity,
            R.style.TextContent_BodyLarge_Bold
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        observeFavoriteUsers()
    }

    private fun observeFavoriteUsers() {
        viewModel.favoriteUsers.observe(this) { users ->
            if (users.isNotEmpty()) {
                showEmptyState(false)

                val items = arrayListOf<UserItems>()
                users.map {
                    val item = UserItems(
                        login = it.username,
                        type = it.type,
                        avatarUrl = it.avatarUrl,
                        url = it.userUrl
                    )
                    items.add(item)
                }
                setListFavoriteUser(items)
            } else {
                showEmptyState(true)
            }
        }
    }

    private fun setListFavoriteUser(user: List<UserItems>) {
        val layoutManager = LinearLayoutManager(this)
        val adapter = ListUserAdapter()
        binding.apply {
            rvUser.layoutManager = layoutManager
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }
        adapter.submitList(user)
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.apply {
            ivEmptyFavorite.visibility = if (isEmpty) View.VISIBLE else View.GONE
            rvUser.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}