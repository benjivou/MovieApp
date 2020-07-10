package com.example.movieapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.crawler.pojo.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(private val list: List<Movie>) : RecyclerView.Adapter<MovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = list[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = list.size

}

class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {

    private val TAG = "ListAdapter"

    private var titleView: TextView? = null
    private var popularity: TextView? = null
    private var rate: TextView? = null
    private var image: ImageButton? = null

    init {

        titleView = itemView.list_title
        popularity = itemView.list_popularity
        rate = itemView.list_rate
        image = itemView.image
    }

    fun bind(movie: Movie) {
        titleView?.text = movie.title
        popularity?.text = "popularity : ${movie.popularity}"
        rate?.text = "rate  : ${movie.voteAverage}"
        Picasso.get().load("https://image.tmdb.org/t/p/w185${movie.posterPath}").into(image);
    }

}