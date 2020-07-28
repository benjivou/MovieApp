package com.example.movieapp.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieapp.ui.viewmodel.MainViewModel

/**
 * Created by Benjamin Vouillon on 23,July,2020
 */
abstract class AbstractFragment : Fragment() {
    protected val viewModel: MainViewModel by activityViewModels()
}
