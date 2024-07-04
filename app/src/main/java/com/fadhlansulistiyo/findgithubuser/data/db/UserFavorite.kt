package com.fadhlansulistiyo.findgithubuser.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_favorite")
data class UserFavorite(
    @ColumnInfo(name = "username")
    @PrimaryKey(autoGenerate = false)
    var username: String,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String,

    @ColumnInfo(name = "type")
    var type: String,

    @ColumnInfo(name = "user_url")
    var userUrl: String
)