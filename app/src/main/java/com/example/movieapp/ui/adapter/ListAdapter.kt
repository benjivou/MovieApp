package com.example.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.entities.displayabledata.SuccessMoviePrepared
import com.example.movieapp.data.model.Movie
import com.example.movieapp.ui.viewmodel.MainViewModel

class ListAdapter(
    private val context: Context,
    private val moviesViewHolderListener: MovieViewHolder.MoviesViewHolderListener
) :
    RecyclerView.Adapter<MovieViewHolder>() {

    private var list: List<SuccessMoviePrepared<Movie>> = listOf()

    // change data and notify the change to RecyclerView
    fun changeData(list: List<SuccessMoviePrepared<Movie>>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false),
            moviesViewHolderListener
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}


