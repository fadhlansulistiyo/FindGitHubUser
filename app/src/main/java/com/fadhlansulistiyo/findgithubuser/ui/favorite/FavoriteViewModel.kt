package com.fadhlansulistiyo.findgithubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository
import com.fadhlansulistiyo.findgithubuser.data.db.UserFavorite

class FavoriteViewModel(repository: UserRepository) : ViewModel() {
    val favoriteUsers: LiveData<List<UserFavorite>> = repository.getAllFavorites()
}