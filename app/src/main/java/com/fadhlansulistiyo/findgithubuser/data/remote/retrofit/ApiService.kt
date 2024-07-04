package com.fadhlansulistiyo.findgithubuser.data.remote.retrofit

import com.fadhlansulistiyo.findgithubuser.data.remote.response.DetailUserResponse
import com.fadhlansulistiyo.findgithubuser.data.remote.response.FollowResponseItem
import com.fadhlansulistiyo.findgithubuser.data.remote.response.SearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getSearchUser(
        @Query("q") q: String
    ): SearchUserResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailUserResponse

    @GET("users/{username}/following")
    suspend fun getFollowingList(
        @Path("username") username: String
    ): List<FollowResponseItem>

    @GET("users/{username}/followers")
    suspend fun getFollowersList(
        @Path("username") username: String
    ): List<FollowResponseItem>
}