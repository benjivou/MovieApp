package com.example.movieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.movieapp.R
import com.example.movieapp.databinding.DetailFragmentBinding
import com.example.movieapp.ui.viewmodel.DetailViewModel
import com.example.movieapp.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.detail_fragment.*

/**
 * Created by Benjamin Vouillon on 23,July,2020
 */
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by activityViewModels()
    private val args:DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        DetailFragmentBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idMovie = args.StringAttributIdUser

        title.text  = idMovie.toString()

    }
}