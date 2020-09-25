package com.example.chalengesport.service.api

import com.google.gson.annotations.SerializedName

data class BaseErrorResponse(
    @SerializedName("status")
    var status : Int? = null,
    @SerializedName("message")
    var message:String? = null,
    @SerializedName("error")
    var error: Boolean? = null
)