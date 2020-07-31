package com.example.movieapp.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.entities.displayabledata.SuccessMoviePrepared
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.ListItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
const val PURL = "https://image.tmdb.org/t/p/w185"
private val TAG = "MovieViewHolder"

class MovieViewHolder(
    itemView: View,
    private val moviesViewHolderListener: MoviesViewHolderListener
) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemBinding.bind(itemView)
    private var movie: Movie? = null


    fun bind(successMoviePrepared: SuccessMoviePrepared<Movie>) {

        this.movie = successMoviePrepared.body

        binding.apply {
            itemView.resources.apply {

                listTitle.text = movie!!.title

                listPopularity.text =
                    getString(R.string.itemPopularity, movie!!.popularity.toString())

                listRate.text =
                    getString(R.string.itemRate, movie!!.voteAverage.toString())

                Picasso.get().load(PURL + movie!!.posterPath)
                    .into(image)
            }
        }

        binding.likeBtn.setOnClickListener(
            View.OnClickListener {
                moviesViewHolderListener?.onItemLiked(movie!!)
            }
        )

        binding.image.setOnClickListener(View.OnClickListener {
            moviesViewHolderListener.onDetailsRequested(it, movie!!)
        })

        if (successMoviePrepared.isLiked) {
            binding.likeBtn.setImageResource(R.drawable.ic_favorite_black_18dp)
        } else {
            binding.likeBtn.setImageResource(R.drawable.ic_favorite_border_black_18dp)
        }
    }

    interface MoviesViewHolderListener {
        fun onItemLiked(movie: Movie)
        fun onDetailsRequested(
            view: View,
            movie: Movie
        )
    }
}