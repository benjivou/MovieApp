package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R.string.itemPopularity
import com.example.movieapp.R.string.itemRate
import com.example.movieapp.crawler.pojo.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter() : RecyclerView.Adapter<MovieViewHolder>() {

    private var list: List<Movie> = listOf()

    // change data and notify the change to RecyclerView
    fun changeData(list: List<Movie>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = list[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = list.size

}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(movie: Movie) {

        itemView.run {

            listTitle.text = movie.title

            listPopularity.text =
                resources.getString(itemPopularity, movie.popularity.toString())

            listRate.text =
                resources.getString(itemRate, movie.voteAverage.toString())

            Picasso.get().load("https://image.tmdb.org/t/p/w185${movie.posterPath}")
                .into(image)
        }


    }

}