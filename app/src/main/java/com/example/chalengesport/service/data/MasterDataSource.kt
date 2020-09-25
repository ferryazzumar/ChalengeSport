package com.example.chalengesport.service.data

import com.example.chalengesport.commons.models.AllTeamListModel
import com.example.chalengesport.commons.models.DetailTeamModel
import com.example.chalengesport.service.Resource

interface MasterDataSource {
    suspend fun getAllTeam(l:String) : Resource<List<AllTeamListModel.Team>>

    suspend fun  getDetailTeam(id:String?) : Resource<List<DetailTeamModel.Team>>
}