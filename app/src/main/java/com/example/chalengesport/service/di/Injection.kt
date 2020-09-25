package com.myapps.esportapp.service.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.chalengesport.service.data.MasterItemRepository
import com.example.chalengesport.service.data.remote.MasterRemoteDataSource


object Injection {

    fun injectViewModel(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(masterItemRepository(context))
    }

//    private fun initLocal(context: Context): MasterLocalDataSource {
//        val database = PanintiStoreDatabase(context)
//        return MasterLocalDataSource(database.panintiDatabaseDao())
//    }

    private fun masterItemRepository(context: Context): MasterItemRepository? {
        val remoteDataSource = MasterRemoteDataSource()
        return MasterItemRepository(remoteDataSource).getInstance()
    }


}