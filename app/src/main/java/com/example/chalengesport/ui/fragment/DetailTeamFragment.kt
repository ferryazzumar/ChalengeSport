package com.example.chalengesport.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.chalengesport.R
import com.example.chalengesport.commons.utils.ViewUtils.Companion.loadImage
import com.example.chalengesport.commons.utils.ViewUtils.Companion.textOrNull
import com.example.chalengesport.service.Resource
import com.example.chalengesport.ui.viewmodel.TeamViewModel
import com.myapps.esportapp.service.di.Injection
import kotlinx.android.synthetic.main.fragment_detail_team.*
import kotlinx.coroutines.*

class DetailTeamFragment : Fragment(R.layout.fragment_detail_team) {

    private val args by navArgs<DetailTeamFragmentArgs>()

    private lateinit var teamViewModel: TeamViewModel

    private var coroutineScope = CoroutineScope(Dispatchers.Main) + Job()

    private fun obtainViewModel(): TeamViewModel {
        return ViewModelProvider(this, Injection.injectViewModel(requireContext())).get(
            TeamViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamViewModel = obtainViewModel()

//        Toast.makeText(context, args.idTeam, Toast.LENGTH_LONG).show()


        coroutineScope.launch {
            initLogicFetchData()
            initUi().join()
        }
    }

    private suspend fun initLogicFetchData() {
        teamViewModel.getDetailTeam(args.idTeam)
    }

    private fun initUi() = coroutineScope.launch {
        getDetailTeam()
    }

    private fun getDetailTeam() {

        teamViewModel.detailTeamList.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    tvTeamName.textOrNull(response.data?.get(0)?.strTeam)
                    tvTeamDesc.textOrNull(response.data?.get(0)?.strDescriptionEN)
                    ivTeamBadge.loadImage(response.data?.get(0)?.strTeamBadge, scaleType = ImageView.ScaleType.FIT_CENTER)
                    tvCaptionBadge.textOrNull(response.data?.get(0)?.strTeam + " Team Badge")
                    tvStadiumTitle.textOrNull(response.data?.get(0)?.strStadium)
                    ivStadium.loadImage(response.data?.get(0)?.strStadiumThumb, scaleType = ImageView.ScaleType.FIT_CENTER)
                    tvCaptionStadium.textOrNull(response.data?.get(0)?.strTeam + " Stadium")
                    tvStadiumDesc.textOrNull(response.data?.get(0)?.strStadiumDescription)
                    ivTeamJersey.loadImage(response.data?.get(0)?.strTeamJersey, scaleType = ImageView.ScaleType.FIT_CENTER)
                    tvCaptionJersey.textOrNull(response.data?.get(0)?.strTeam + " Team Jersey")
                    tvDataSport.textOrNull(response.data?.get(0)?.strSport)
                    tvDataLeague.textOrNull(response.data?.get(0)?.strLeague + ", " + response.data?.get(0)?.strLeague2 + ", " + response.data?.get(0)?.strLeague3 + ", " + response.data?.get(0)?.strLeague4 + ", " + response.data?.get(0)?.strLeague5)
                    tvDataStadium.textOrNull(response.data?.get(0)?.strStadium + ", " + response.data?.get(0)?.strStadiumLocation)
                }
                is Resource.Error -> {
                    Log.e("Error", response.message.toString())
                }
            }
        })
    }
}