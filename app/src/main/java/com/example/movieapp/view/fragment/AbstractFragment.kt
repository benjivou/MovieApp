package com.example.movieapp.view.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieapp.viewModel.MainViewModel

/**
 * Created by Benjamin Vouillon on 23,July,2020
 */
abstract class AbstractFragment : Fragment() {
    protected val viewModel: MainViewModel by activityViewModels()
}
