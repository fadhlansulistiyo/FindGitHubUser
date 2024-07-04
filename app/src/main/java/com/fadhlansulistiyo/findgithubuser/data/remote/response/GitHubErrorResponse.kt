package com.fadhlansulistiyo.findgithubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class GitHubErrorResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("documentation_url")
	val documentationUrl: String,

	@field:SerializedName("status")
	val status: String
)
