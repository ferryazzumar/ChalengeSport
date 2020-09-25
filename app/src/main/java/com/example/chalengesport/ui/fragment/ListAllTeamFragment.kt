package com.example.chalengesport.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chalengesport.R
import com.example.chalengesport.service.Resource
import com.example.chalengesport.ui.adapter.AllTeamListAdapter
import com.example.chalengesport.ui.viewmodel.TeamViewModel
import com.myapps.esportapp.service.di.Injection
import kotlinx.android.synthetic.main.fragment_list_all_team.*
import kotlinx.coroutines.*


class ListAllTeamFragment : Fragment(R.layout.fragment_list_all_team) {

    private lateinit var teamViewModel: TeamViewModel

    private val allTeamListAdapter = AllTeamListAdapter()

    private var coroutineScope = CoroutineScope(Dispatchers.Main) + Job()

    private fun obtainViewModel(): TeamViewModel {
        return ViewModelProvider(this, Injection.injectViewModel(requireContext())).get(
            TeamViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamViewModel = obtainViewModel()

        coroutineScope.launch {
            initLogicFetchData()
            initUi().join()
        }

    }

    private suspend fun initLogicFetchData() {
        teamViewModel.getAllTeamList("English Premier League")
    }

    private fun initUi() = coroutineScope.launch {
        getAllTeamList()
    }

    private fun getAllTeamList() {

        teamViewModel.allTeamList.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    allTeamListAdapter.differ.submitList(response.data ?: listOf())
                }
                is Resource.Error -> {
                    Log.e("Error", response.message.toString())
                }
            }
        })

        rcvListTeam.apply {
            adapter = allTeamListAdapter
            this.layoutManager = GridLayoutManager(
                context,
                3
            )
        }
    }
}
