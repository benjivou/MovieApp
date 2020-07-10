package com.example.movieapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.crawler.pojo.Movie
import com.example.movieapp.viewModel.MainViewModel
import com.example.movieapp.viewModel.TypeDisplay
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

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
                    R.id.mode_like -> TypeDisplay.LIKED
                    R.id.mode_popular -> TypeDisplay.POPULAR
                    R.id.mode_rated -> TypeDisplay.RATED
                    else -> TypeDisplay.POPULAR
                }
            )
        )


        return true
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private fun loadListOfMovies(movies: MutableLiveData<List<Movie>>) {
        list_recycler_view.apply {
            layoutManager = GridLayoutManager(activity, 2)
            movies
                .observe(viewLifecycleOwner, Observer { adapter = ListAdapter(it) })
        }
    }
}