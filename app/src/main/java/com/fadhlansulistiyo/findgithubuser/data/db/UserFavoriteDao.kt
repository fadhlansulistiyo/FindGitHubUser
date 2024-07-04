package com.fadhlansulistiyo.findgithubuser.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(userFavorite: UserFavorite)

    @Delete
    suspend fun removeFavorite(userFavorite: UserFavorite)

    @Query("SELECT * FROM user_favorite WHERE username = :username LIMIT 1")
    suspend fun getFavoriteUserByUsername(username: String): UserFavorite?

    @Query("SELECT * FROM user_favorite")
    fun getAllFavorites(): LiveData<List<UserFavorite>>
}