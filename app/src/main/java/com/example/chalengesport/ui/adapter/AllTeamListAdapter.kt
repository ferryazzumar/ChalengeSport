package com.example.chalengesport.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chalengesport.R
import com.example.chalengesport.commons.models.AllTeamListModel
import com.example.chalengesport.commons.utils.ViewUtils.Companion.loadImage
import com.example.chalengesport.commons.utils.ViewUtils.Companion.textOrNull
import com.example.chalengesport.ui.fragment.ListAllTeamFragmentDirections
import kotlinx.android.synthetic.main.item_list_team.view.*

class AllTeamListAdapter :RecyclerView.Adapter<AllTeamListAdapter.AllTeamViewHolder>(){
  inner class AllTeamViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
      fun bind(item:AllTeamListModel.Team){
          with(itemView){
              ivTeamLogo.loadImage(item.strTeamBadge,scaleType = ImageView.ScaleType.FIT_CENTER)
              tvTeamName.textOrNull(item.strTeam)

              var idTeamChoosed = item.idTeam

              setOnClickListener {
                  findNavController().navigate(ListAllTeamFragmentDirections.actionListToDetail(idTeamChoosed.toString()))
              }
          }
      }
  }

    private val differCallBack = object : DiffUtil.ItemCallback<AllTeamListModel.Team>() {
        override fun areItemsTheSame(
            oldItem: AllTeamListModel.Team,
            newItem: AllTeamListModel.Team
        ): Boolean {
            return oldItem.idTeam == newItem.idTeam
        }

        override fun areContentsTheSame(
            oldItem: AllTeamListModel.Team,
            newItem: AllTeamListModel.Team
        ): Boolean {
            return oldItem == newItem        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllTeamListAdapter.AllTeamViewHolder {
        return AllTeamViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_team,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
    return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AllTeamListAdapter.AllTeamViewHolder, position: Int) {
        holder.bind(
            differ.currentList[position]
        )
    }
    private var onItemClickListener: ((AllTeamListModel.Team) -> Unit)? = null

    fun setOnItemClickListener(listener: (AllTeamListModel.Team) -> Unit) {
        onItemClickListener = listener
    }
}