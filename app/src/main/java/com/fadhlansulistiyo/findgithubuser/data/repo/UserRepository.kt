package com.fadhlansulistiyo.findgithubuser.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fadhlansulistiyo.findgithubuser.data.db.UserFavorite
import com.fadhlansulistiyo.findgithubuser.data.db.UserFavoriteDao
import com.fadhlansulistiyo.findgithubuser.data.remote.response.DetailUserResponse
import com.fadhlansulistiyo.findgithubuser.data.remote.response.FollowResponseItem
import com.fadhlansulistiyo.findgithubuser.data.remote.response.GitHubErrorResponse
import com.fadhlansulistiyo.findgithubuser.data.remote.response.RateLimitExceededException
import com.fadhlansulistiyo.findgithubuser.data.remote.response.SearchUserResponse
import com.fadhlansulistiyo.findgithubuser.data.remote.retrofit.ApiService
import com.fadhlansulistiyo.findgithubuser.data.setting.SettingPreferences
import com.google.gson.Gson
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userFavoriteDao: UserFavoriteDao,
    private val pref: SettingPreferences
) {

    suspend fun getSearchUser(username: String): SearchUserResponse {
        return try {
            val response = apiService.getSearchUser(username)
            if (response.totalCount == 0) {
                throw Exception(MESSAGE_ERROR)
            } else {
                response
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        }
    }

    suspend fun getDetailUser(username: String): DetailUserResponse {
        return try {
            val response = apiService.getDetailUser(username)
            response
        } catch (e: HttpException) {
            handleHttpException(e)
        }
    }

    suspend fun getFollowingList(username: String): List<FollowResponseItem> {
        return try {
            val response = apiService.getFollowingList(username)
            response
        } catch (e: HttpException) {
            handleHttpException(e)
        }
    }

    suspend fun getFollowersList(username: String): List<FollowResponseItem> {
        return try {
            val response = apiService.getFollowersList(username)
            response
        } catch (e: HttpException) {
            handleHttpException(e)
        }
    }

    /*---User Favorite---*/
    suspend fun addFavorite(userFavorite: UserFavorite) {
        userFavoriteDao.addFavorite(userFavorite)
    }

    suspend fun removeFavorite(userFavorite: UserFavorite) {
        userFavoriteDao.removeFavorite(userFavorite)
    }

    suspend fun getFavoriteUserByUsername(username: String): UserFavorite? {
        return userFavoriteDao.getFavoriteUserByUsername(username)
    }

    fun getAllFavorites(): LiveData<List<UserFavorite>> {
        return userFavoriteDao.getAllFavorites()
    }

    /*---THEME SETTING---*/
    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        pref.saveThemeSetting(isDarkModeActive)
    }

    private fun handleHttpException(e: HttpException): Nothing {
        val errorBody = e.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, GitHubErrorResponse::class.java)
        if (errorResponse?.message?.contains("API rate limit exceeded") == true) {
            throw RateLimitExceededException(errorResponse.message)
        } else {
            throw Exception(MESSAGE_ERROR)
        }
    }

    companion object {
        const val MESSAGE_ERROR = "User not Found. Please enter the username correctly."
        const val MESSAGE_RATE_LIMIT_EXCEEDED = "Rate limit exceeded. Please try again later."

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userFavoriteDao: UserFavoriteDao,
            pref: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userFavoriteDao, pref)
            }.also { instance = it }
    }
}