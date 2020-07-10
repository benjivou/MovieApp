package com.example.movieapp

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.viewModel.MainViewModel

abstract class SingleFragmentActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val layoutResId: Int
        @LayoutRes
        get() = R.layout.activity_fragment

    protected abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResId)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        // ensures fragments already created will not be created
        if (fragment == null) {
            fragment = createFragment()
            // create and commit a fragment transaction
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}