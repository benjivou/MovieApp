package com.example.movieapp.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.ListItemBinding
import com.example.movieapp.ui.viewmodel.MainViewModel
import com.squareup.picasso.Picasso

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
const val PURL = "https://image.tmdb.org/t/p/w185"
private val TAG = "MovieViewHolder"

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemBinding.bind(itemView)
    private var movie: Movie? = null


    fun bind(pair: Pair<Movie, Boolean>, mainViewModel: MainViewModel) {

        this.movie = pair.first

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
            if (pair.second)
                View.OnClickListener {
                    mainViewModel.deleteMovie(movie!!)
                }
            else
                View.OnClickListener {
                    mainViewModel.insertMovie(movie!!)
                }

        )

        if (pair.second) {
            binding.likeBtn.setImageResource(R.drawable.ic_favorite_black_18dp)
        } else {
            binding.likeBtn.setImageResource(R.drawable.ic_favorite_border_black_18dp)
        }
    }


}