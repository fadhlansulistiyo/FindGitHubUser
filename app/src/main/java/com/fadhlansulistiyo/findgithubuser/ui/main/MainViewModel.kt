package com.fadhlansulistiyo.findgithubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlansulistiyo.findgithubuser.data.remote.response.RateLimitExceededException
import com.fadhlansulistiyo.findgithubuser.data.remote.response.SearchUserResponse
import com.fadhlansulistiyo.findgithubuser.data.repo.ResultState
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository
import com.fadhlansulistiyo.findgithubuser.data.repo.UserRepository.Companion.MESSAGE_RATE_LIMIT_EXCEEDED
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<ResultState<SearchUserResponse>>()
    val searchResults: LiveData<ResultState<SearchUserResponse>> get() = _searchResults

    fun getSearchUser(username: String) {
        _searchResults.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getSearchUser(username)
                if (response.totalCount == 0) {
                    _searchResults.postValue(ResultState.Error(UserRepository.MESSAGE_ERROR))
                } else {
                    _searchResults.postValue(ResultState.Success(response))
                }
            } catch (e: RateLimitExceededException) {
                _searchResults.postValue(ResultState.Error(MESSAGE_RATE_LIMIT_EXCEEDED))
            } catch (e: Exception) {
                _searchResults.postValue(ResultState.Error(UserRepository.MESSAGE_ERROR))
            }
        }
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return repository.getThemeSetting()
    }
}