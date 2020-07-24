package com.example.movieapp.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMainBinding
import com.example.movieapp.model.TypeDisplay
import com.example.movieapp.view.ListAdapter
import com.example.movieapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapterList: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        FragmentMainBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterList = ListAdapter(requireContext(), viewModel)
        // setup the RecyclerView
        listRecyclerView.apply {

            layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.numberspan))
            // init the adapter to the good value
            adapter = adapterList
        }

        viewModel
            .getListCurrent()
            .observe(viewLifecycleOwner, Observer { movies ->
                adapterList.changeData(movies)
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_action_bar, menu)
    }

    /**
     * the idea is to the current list display and get the new list
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // change the list in the modelView
        viewModel.getList(
            when (item.itemId) {
                R.id.displayMoviesLiked -> TypeDisplay.LIKED
                R.id.displayMoviesPopular -> TypeDisplay.POPULAR
                R.id.displayMoviesMostRated -> TypeDisplay.RATED
                else -> TypeDisplay.POPULAR
            }
        )
        return true
    }


}

