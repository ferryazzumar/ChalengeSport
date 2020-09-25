package com.example.chalengesport.service.api

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data") val data : T
) : BaseApiResponse()