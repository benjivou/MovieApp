package com.example.movieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.movieapp.R
import com.example.movieapp.databinding.DetailFragmentBinding
import com.example.movieapp.ui.adapter.PURL
import com.example.movieapp.ui.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso


/**
 * Created by Benjamin Vouillon on 23,July,2020
 */
private const val TAG = "DetailFragment"

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        DetailFragmentBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DetailFragmentBinding.bind(view)
        val idMovie = args.StringAttributIdUser

        viewModel.getMovieAndIsLiked(idMovie)
            .observe(viewLifecycleOwner, Observer { (first, second) ->
                binding.apply {
                    first?.let {
                        title.text = it.title
                        overview.text = it.overview
                        Picasso.get().load(PURL + it.posterPath).into(image)
                        userRating.text =
                            resources.getString(R.string.itemRate, it.voteAverage.toString())
                        realeseDate.text = it.releaseDate

                        if (second) {
                            likeBtn.setImageResource(R.drawable.ic_favorite_black_18dp)
                        } else {
                            likeBtn.setImageResource(R.drawable.ic_favorite_border_black_18dp)
                        }

                        likeBtn.setOnClickListener {
                            viewModel.onLikeButtonClicked()
                        }
                    }
                }
            })
    }

    interface DetailFragmentListener {
        fun onLikeButtonClicked()
    }
}