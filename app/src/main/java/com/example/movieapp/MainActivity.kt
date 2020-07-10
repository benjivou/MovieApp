package com.example.movieapp

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.viewModel.MainViewModel

class MainActivity : SingleFragmentActivity() {


    override fun createFragment() = MainFragment.newInstance()




}
