package com.kl3jvi.animity.ui.fragments.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomSearchAdapter
import com.kl3jvi.animity.ui.base.viewBinding
import com.kl3jvi.animity.utils.collectLatestFlow
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    val viewModel: SearchViewModel by viewModels()
    val binding: FragmentSearchBinding by viewBinding()

    private lateinit var searchAdapter: CustomSearchAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            searchRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                searchAdapter = CustomSearchAdapter()
                adapter = searchAdapter
            }
            mainSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    search(query)
                    return false
                }
            })
        }
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            collectLatestFlow(viewModel.searchAnimes(query)) { animeData ->
                searchAdapter.submitData(animeData)
                binding.searchRecycler.adapter = searchAdapter
                searchAdapter.addLoadStateListener { loadState ->
                    if (loadState.append.endOfPaginationReached) {
                        if (searchAdapter.itemCount < 1) {
                            binding.searchRecycler.hide()
                            binding.noSearchResult.show()
                        } else {
                            binding.noSearchResult.hide()
                        }
                    } else {
                        binding.searchRecycler.show()
                    }
                }
            }
        }
    }
}
