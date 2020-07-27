package com.example.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.ui.viewmodel.MainViewModel

class ListAdapter(private val context: Context, private val itemLiked: MovieViewHolder.ItemLiked) :
    RecyclerView.Adapter<MovieViewHolder>() {

    private var list: List<Pair<Movie, Boolean>> = listOf()

    // change data and notify the change to RecyclerView
    fun changeData(list: List<Pair<Movie, Boolean>>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position], itemLiked)
    }

    override fun getItemCount(): Int = list.size

}


