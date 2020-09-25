package com.example.chalengesport.service.api

import com.example.chalengesport.commons.models.AllTeamListModel
import com.example.chalengesport.commons.models.DetailTeamModel
import com.example.chalengesport.commons.utils.other.Constants
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ESportApiService {
    @POST(Constants.listTeamURL)
    suspend fun getListAllTeam(
        @Query("l") l:String ?
    ): Response<AllTeamListModel>

    @POST(Constants.detailTeamURL)
    suspend fun getTeamDetail(
        @Query("id") id:String?
    ):Response<DetailTeamModel>
}