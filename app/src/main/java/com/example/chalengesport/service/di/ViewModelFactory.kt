package com.myapps.esportapp.service.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chalengesport.service.data.MasterItemRepository
import com.example.chalengesport.ui.viewmodel.TeamViewModel

class ViewModelFactory (private val masterItemRepository: MasterItemRepository?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TeamViewModel::class.java) -> TeamViewModel(masterItemRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}