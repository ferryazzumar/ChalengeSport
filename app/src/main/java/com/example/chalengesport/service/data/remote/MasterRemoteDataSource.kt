package com.example.chalengesport.service.data.remote

import com.example.chalengesport.commons.models.AllTeamListModel
import com.example.chalengesport.commons.models.DetailTeamModel
import com.example.chalengesport.commons.utils.Utils.Companion.parse
import com.example.chalengesport.commons.utils.Utils.Companion.string
import com.example.chalengesport.service.api.BaseErrorResponse
import com.example.chalengesport.service.api.RetrofitInstance.Companion.BASE_API


class MasterRemoteDataSource {
    suspend fun fetchListAllTeamResponse(l: String): com.example.chalengesport.service.Resource<List<AllTeamListModel.Team>> {
        val response = BASE_API.getListAllTeam(l)
        if (response.isSuccessful) {
            response.body()?.let {
                return com.example.chalengesport.service.Resource.Success(it.teams)
            }
        }
        return com.example.chalengesport.service.Resource.Error(
            response.errorBody()
                ?.parse<BaseErrorResponse>()
                ?.message.string("An Error occurred !")
        )
    }
    suspend fun fetchDetailTeam(id:String) : com.example.chalengesport.service.Resource<List<DetailTeamModel.Team>>{
        val response = BASE_API.getTeamDetail(id)
        if (response.isSuccessful){
            response.body()?.let{
                return com.example.chalengesport.service.Resource.Success(it.teams)
            }
        }
        return com.example.chalengesport.service.Resource.Error(
            response.errorBody()
                ?.parse<BaseErrorResponse>()
                ?.message.string("An Error occurred !")
        )
    }
}
