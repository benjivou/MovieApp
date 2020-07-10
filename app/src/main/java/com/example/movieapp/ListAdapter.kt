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

    private var mTitleView: TextView? = null
    private var mPopularity: TextView? = null
    private var mRate: TextView? = null
    private var mImage: ImageButton? = null

    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mPopularity = itemView.findViewById(R.id.list_popularity)
        mRate = itemView.findViewById(R.id.list_rate)
        mImage = itemView.image
    }

    fun bind(movie: Movie) {
        mTitleView?.text = movie.title
        mPopularity?.text = "popularity : ${ movie.popularity}"
        mRate?.text = "rate  : ${ movie.voteAverage}"
        Picasso.get().load("https://image.tmdb.org/t/p/w185${movie.posterPath}").into(mImage);
    }

}