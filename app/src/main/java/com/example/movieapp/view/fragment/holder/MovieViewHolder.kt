package com.example.movieapp.view.fragment.holder

import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.ListItemBinding
import com.example.movieapp.likesmanager.App
import com.example.movieapp.model.Movie
import com.example.movieapp.view.fragment.MainFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
const val PURL = "https://image.tmdb.org/t/p/w185"
private val TAG = "MovieViewHolder"

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemBinding.bind(itemView)
    private var movie: Movie? = null


    fun bind(movie: Movie, positionInCurrentList: Int) {

        this.movie = movie

        binding.apply {
            itemView.resources.apply {

                listTitle.text = movie.title

                listPopularity.text =
                    getString(R.string.itemPopularity, movie.popularity.toString())

                listRate.text =
                    getString(R.string.itemRate, movie.voteAverage.toString())

                Picasso.get().load(PURL + movie.posterPath)
                    .into(image)
            }
        }
        binding.likeBtn.setOnClickListener(
            LikeClicker(
                movie,
                binding
            )
        )
        binding.image.setOnClickListener(
            PictureClicker(movie, binding)
        )
    }

    class LikeClicker(val movie: Movie, private val binding: ListItemBinding) :
        View.OnClickListener {

        init {
            MainScope().launch(Dispatchers.IO) {
                if (App.database.movieDAO().isLiked(movie.id)) {
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_black_18dp)
                } else {
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_border_black_18dp)
                }
            }
        }

        override fun onClick(v: View?) {

            MainScope().launch(Dispatchers.IO) {
                if (App.database.movieDAO().isLiked(movie.id)) {
                    movie.let { it1 -> App.database.movieDAO().deleteMovie(it1) }
                    Log.d(TAG, "onClick: delete done")
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_border_black_18dp)
                } else {
                    movie.let { it1 -> App.database.movieDAO().insertMovie(it1) }
                    Log.d(TAG, "onClick: job done")
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_black_18dp)
                }
            }
        }
    }

    class PictureClicker(val movie: Movie, private val binding: ListItemBinding) :
        View.OnClickListener {
        override fun onClick(v: View?) {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
            val toto =
                binding.root.findNavController().navigate(action)
            Log.d(TAG, "onClick: the image is clicked")

        }

    }


}