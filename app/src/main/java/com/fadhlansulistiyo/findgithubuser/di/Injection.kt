package com.fadhlansulistiyo.findgithubuser.di

import android.content.Context
import com.fadhlansulistiyo.findgithubuser.data.db.UserDatabase
import com.fadhlansulistiyo.findgithubuser.data.remote.retrofit.ApiConfig
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository
import com.fadhlansulistiyo.findgithubuser.data.setting.SettingPreferences
import com.fadhlansulistiyo.findgithubuser.data.setting.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userFavoriteDao()
        val settingPreference = SettingPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, dao, settingPreference)
    }
}