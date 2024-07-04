package com.fadhlansulistiyo.findgithubuser.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository
import com.fadhlansulistiyo.findgithubuser.di.Injection
import com.fadhlansulistiyo.findgithubuser.ui.detailuser.DetailUserViewModel
import com.fadhlansulistiyo.findgithubuser.ui.favorite.FavoriteViewModel
import com.fadhlansulistiyo.findgithubuser.ui.follow.FollowViewModel
import com.fadhlansulistiyo.findgithubuser.ui.main.MainViewModel
import com.fadhlansulistiyo.findgithubuser.ui.setting.SettingViewModel

class ViewModelFactory private constructor(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository) as T
            modelClass.isAssignableFrom(DetailUserViewModel::class.java) -> DetailUserViewModel(repository) as T
            modelClass.isAssignableFrom(FollowViewModel::class.java) -> FollowViewModel(repository) as T
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel(repository) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}