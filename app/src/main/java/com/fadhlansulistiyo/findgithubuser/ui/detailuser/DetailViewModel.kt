package com.fadhlansulistiyo.findgithubuser.ui.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlansulistiyo.findgithubuser.data.db.UserFavorite
import com.fadhlansulistiyo.findgithubuser.data.remote.response.DetailUserResponse
import com.fadhlansulistiyo.findgithubuser.data.remote.response.RateLimitExceededException
import com.fadhlansulistiyo.findgithubuser.data.repo.ResultState
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository.Companion.MESSAGE_RATE_LIMIT_EXCEEDED
import kotlinx.coroutines.launch

class DetailUserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _detailUser = MutableLiveData<ResultState<DetailUserResponse>>()
    val detailUser: LiveData<ResultState<DetailUserResponse>> get() = _detailUser

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun getDetailUser(username: String) {
        _detailUser.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getDetailUser(username)
                _detailUser.postValue(ResultState.Success(response))

                checkIfFavorite(username)
            } catch (e: RateLimitExceededException) {
                _detailUser.postValue(ResultState.Error(MESSAGE_RATE_LIMIT_EXCEEDED))
            } catch (e: Exception) {
                _detailUser.postValue(ResultState.Error(UserRepository.MESSAGE_ERROR))
            }
        }
    }

    fun setUserFavorite(userFavorite: UserFavorite) {
        viewModelScope.launch {
            val favorite = repository.getFavoriteUserByUsername(userFavorite.username)
            if (favorite == null) {
                repository.addFavorite(userFavorite)
                _isFavorite.postValue(true)
            } else {
                repository.removeFavorite(userFavorite)
                _isFavorite.postValue(false)
            }
        }
    }

    private fun checkIfFavorite(username: String) {
        viewModelScope.launch {
            val favorite = repository.getFavoriteUserByUsername(username)
            _isFavorite.postValue(favorite != null)
        }
    }
}