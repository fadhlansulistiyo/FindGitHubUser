package com.fadhlansulistiyo.findgithubuser.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: UserRepository) : ViewModel() {

    fun getThemeSetting(): LiveData<Boolean> {
        return repository.getThemeSetting()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }
}