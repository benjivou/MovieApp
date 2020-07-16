package com.example.movieapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.crawler.pojo.Movie
import com.example.movieapp.databinding.ListItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemBinding.bind(itemView)

    fun bind(movie: Movie) {

        with(itemView.resources) {
            binding.run {
                listTitle.text = movie.title

                listPopularity.text =
                    getString(R.string.itemPopularity, movie.popularity.toString())

                listRate.text =
                    getString(R.string.itemRate, movie.voteAverage.toString())

                Picasso.get().load("https://image.tmdb.org/t/p/w185${movie.posterPath}")
                    .into(image)
            }
        }
    }


}