package com.example.chalengesport.service.data

import com.example.chalengesport.commons.models.AllTeamListModel
import com.example.chalengesport.commons.models.DetailTeamModel
import com.example.chalengesport.service.Resource
import com.example.chalengesport.service.data.remote.MasterRemoteDataSource

class MasterItemRepository(private val masterRemoteDataSource: MasterRemoteDataSource) : MasterDataSource {

    @Volatile
    private var INSTANCE: MasterItemRepository? = null

    fun getInstance(): MasterItemRepository? {
        if (INSTANCE == null) {
            synchronized(MasterItemRepository::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = MasterItemRepository(masterRemoteDataSource)
                }
            }
        }
        return INSTANCE
    }

    override suspend fun getAllTeam(l:String): Resource<List<AllTeamListModel.Team>> {
        return masterRemoteDataSource.fetchListAllTeamResponse(l)
    }

    override suspend fun getDetailTeam(id: String?): Resource<List<DetailTeamModel.Team>> {
        return masterRemoteDataSource.fetchDetailTeam(id.toString())
    }
}