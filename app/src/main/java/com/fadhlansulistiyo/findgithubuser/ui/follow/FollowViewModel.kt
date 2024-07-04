package com.fadhlansulistiyo.findgithubuser.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlansulistiyo.findgithubuser.data.remote.response.FollowResponseItem
import com.fadhlansulistiyo.findgithubuser.data.remote.response.RateLimitExceededException
import com.fadhlansulistiyo.findgithubuser.data.repo.ResultState
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository.Companion.MESSAGE_RATE_LIMIT_EXCEEDED
import kotlinx.coroutines.launch

class FollowViewModel(private val repository: UserRepository) : ViewModel() {

    private val _followData = MutableLiveData<ResultState<List<FollowResponseItem>>>()
    val followData: LiveData<ResultState<List<FollowResponseItem>>> get() = _followData

    fun getFollowingList(username: String) {
        _followData.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getFollowingList(username)
                _followData.postValue(ResultState.Success(response))
            } catch (e: RateLimitExceededException) {
                _followData.postValue(ResultState.Error(MESSAGE_RATE_LIMIT_EXCEEDED))
            } catch (e: Exception) {
                _followData.postValue(ResultState.Error(UserRepository.MESSAGE_ERROR))
            }
        }
    }

    fun getFollowersList(username: String) {
        _followData.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getFollowersList(username)
                _followData.postValue(ResultState.Success(response))
            } catch (e: RateLimitExceededException) {
                _followData.postValue(ResultState.Error(MESSAGE_RATE_LIMIT_EXCEEDED))
            } catch (e: Exception) {
                _followData.postValue(ResultState.Error(UserRepository.MESSAGE_ERROR))
            }
        }
    }
}