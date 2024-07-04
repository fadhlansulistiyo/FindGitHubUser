package com.fadhlansulistiyo.findgithubuser.ui.detailuser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fadhlansulistiyo.findgithubuser.R
import com.fadhlansulistiyo.findgithubuser.data.db.UserFavorite
import com.fadhlansulistiyo.findgithubuser.data.remote.response.DetailUserResponse
import com.fadhlansulistiyo.findgithubuser.data.repo.ResultState
import com.fadhlansulistiyo.findgithubuser.databinding.ActivityDetailUserBinding
import com.fadhlansulistiyo.findgithubuser.ui.ViewModelFactory
import com.fadhlansulistiyo.findgithubuser.ui.setting.SettingActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailUserViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // setUp Toolbar
        setSupportActionBar(binding.detailToolbar)
        binding.detailToolbar.setTitleTextAppearance(
            this@DetailUserActivity,
            R.style.TextContent_BodyLarge_Bold
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        //setUp viewPager2 & tabLayout
        setUpSectionsPager()

        // get data user from MainActivity
        val username = intent.getStringExtra(EXTRA_LOGIN)
        val avatarURl = intent.getStringExtra(EXTRA_AVATAR_URL)
        val type = intent.getStringExtra(EXTRA_TYPE)
        val url = intent.getStringExtra(EXTRA_URL)

        if (username != null) {
            viewModel.getDetailUser(username)
        }

        observeData()
        addToFavorite(username.toString(), avatarURl.toString(), type.toString(), url.toString())
        shareDataUser(username.toString(), url.toString())
    }

    private fun observeData() {
        viewModel.detailUser.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    val data = result.data
                    setDetailUserData(data)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showSnackbar(this, result.error)
                }
            }
        }

        viewModel.isFavorite.observe(this) { isFavorite ->
            binding.btnFavorite.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
            )
        }
    }

    private fun setUpSectionsPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.tabLayout.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabLayout.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setDetailUserData(detailUser: DetailUserResponse) {
        binding.apply {
            tvDetailName.text = detailUser.name
            tvDetailUsername.text = detailUser.login
            tvDetailFollowingNum.text = detailUser.following.toString()
            tvDetailFollowersNum.text = detailUser.followers.toString()
            tvDetailReposNum.text = detailUser.publicRepos.toString()
            detailToolbar.title = detailUser.name
        }

        Glide.with(this)
            .load(detailUser.avatarUrl)
            .into(binding.ivDetailAvatar)
    }

    private fun addToFavorite(username: String, avatarUrl: String, type: String, url: String) {
        binding.btnFavorite.setOnClickListener {
            val userFavorite = UserFavorite(username, avatarUrl, type, url)
            viewModel.setUserFavorite(userFavorite)
        }
    }

    private fun shareDataUser(username: String, url: String) {
        binding.detailToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_share -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        val content = "${username}, \n${url}"
                        putExtra(Intent.EXTRA_TEXT, content)
                        this.type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                    true
                }

                R.id.action_settings -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun showSnackbar(activity: Activity, message: String) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}