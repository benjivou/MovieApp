package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R.string.item_pop
import com.example.movieapp.R.string.item_rate
import com.example.movieapp.crawler.pojo.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(private val list: List<Movie>) : RecyclerView.Adapter<MovieViewHolder>() {


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

class MovieViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val TAG = "ListAdapter"


    fun bind(movie: Movie) {
        itemView.list_title.text = movie.title
        itemView.list_popularity.text =
            """${itemView.context.getString(item_pop)} ${movie.popularity}"""
        itemView.list_rate.text =
            """${this.itemView.context.getString(item_rate)} ${movie.voteAverage}"""
        Picasso.get().load("https://image.tmdb.org/t/p/w185${movie.posterPath}")
            .into(itemView.image)

    }

}