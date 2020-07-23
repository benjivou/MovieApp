package com.example.movieapp

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.crawler.pojo.Movie
import com.example.movieapp.databinding.ListItemBinding
import com.example.movieapp.likesmanager.App
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
const val PURL = "https://image.tmdb.org/t/p/w185"
private val TAG = "MovieViewHolder"

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemBinding.bind(itemView)
    private var movie: Movie? = null


    fun bind(movie: Movie) {

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

        binding.likeBtn.setOnClickListener(LikeClicker(movie, binding))
    }

    class LikeClicker(val movie: Movie, private val binding: ListItemBinding) :
        View.OnClickListener {

        private var liked =
            runBlocking(Dispatchers.IO) { App.database.movieDAO().isLiked(movie.id) }

        init {
            if (liked) {
                binding.likeBtn.setImageResource(R.drawable.ic_favorite_black_18dp)
            } else {
                binding.likeBtn.setImageResource(R.drawable.ic_favorite_border_black_18dp)
            }
        }

        override fun onClick(v: View?) {
            // if the check is done ( so liked not null )
            liked = runBlocking(Dispatchers.IO) { App.database.movieDAO().isLiked(movie.id) }

            if (liked) {
                // if the object is already liked
                Thread {
                    movie.let { it1 -> App.database.movieDAO().deleteMovie(it1) }
                }.start()

                binding.likeBtn.setImageResource(R.drawable.ic_favorite_border_black_18dp)
                Log.d(TAG, "onClick: delete done")

            } else {
                Thread {
                    movie.let { it1 -> App.database.movieDAO().insertMovie(it1) }
                    Log.d(TAG, "onClick: job done")

                }.start()
                val toto = App.database.movieDAO().getAll().observeForever {
                    Log.d(
                        TAG,
                        "onClick: display elements ${it.size}"
                    )
                }

                binding.likeBtn.setImageResource(R.drawable.ic_favorite_black_18dp)
            }
        }
    }


}