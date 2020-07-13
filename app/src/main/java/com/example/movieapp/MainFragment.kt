package com.example.movieapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.crawler.pojo.Movie
import com.example.movieapp.frontUtil.isTablet
import com.example.movieapp.viewModel.MainViewModel
import com.example.movieapp.viewModel.TypeDisplay
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listRecyclerView.apply {
            layoutManager =
                if (this.let { activity?.let { it1 -> isTablet(it1) } }!!) GridLayoutManager(
                    activity,
                    3
                ) else GridLayoutManager(activity, 1)

            adapter = ListAdapter()
        }
        loadListOfMovies(viewModel.getListCurrent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_action_bar, menu)
    }

    /**
     * the idea is to the current list display and get the new list
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Unsubscribe to the last LiveData
        viewModel.getListCurrent().removeObserver { }

        // subscribe to the new LiveData
        loadListOfMovies(
            viewModel.getList(
                when (item.itemId) {
                    R.id.displayMoviesLiked -> TypeDisplay.LIKED
                    R.id.displayMoviesPopular -> TypeDisplay.POPULAR
                    R.id.displayMoviesMostRated -> TypeDisplay.RATED
                    else -> TypeDisplay.POPULAR
                }
            )
        )

        return true
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private fun loadListOfMovies(movies: LiveData<List<Movie>>) {
        listRecyclerView.apply {

            movies
                .observe(
                    viewLifecycleOwner,
                    Observer {
                        val adapter = listRecyclerView.adapter as ListAdapter
                        adapter.changeData(it) // change data
                    })
        }

    }
}