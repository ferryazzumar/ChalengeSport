package com.example.chalengesport.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chalengesport.commons.models.AllTeamListModel
import com.example.chalengesport.commons.models.DetailTeamModel
import com.example.chalengesport.service.Resource
import com.example.chalengesport.service.data.MasterItemRepository
import kotlinx.coroutines.launch

class TeamViewModel (private val masterItemRepository: MasterItemRepository?) : ViewModel(){

    val allTeamList: MutableLiveData<Resource<List<AllTeamListModel.Team>>> = MutableLiveData()
    val detailTeamList : MutableLiveData<Resource<List<DetailTeamModel.Team>>> = MutableLiveData()

    suspend fun getAllTeamList(l:String) = viewModelScope.launch {
        allTeamList.postValue(Resource.Loading())

        try {
            val data = masterItemRepository?.getAllTeam(l)
            allTeamList.postValue(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    suspend fun getDetailTeam(id:String?) = viewModelScope.launch {
        detailTeamList.postValue(Resource.Loading())

        try {
            val data = masterItemRepository?.getDetailTeam(id)
            detailTeamList.postValue(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}