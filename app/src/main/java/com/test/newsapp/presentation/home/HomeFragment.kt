package com.test.newsapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.test.newsapp.R
import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.databinding.FragmentHomeBinding
import com.test.newsapp.presentation.base.BaseFragment
import com.test.newsapp.presentation.util.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val adapter: HomeAdapter by lazy { HomeAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initController()
        return binding.root
    }

    override fun initEvent() {
        binding.apply {
            setToolbar()
            rvHome.adapter = adapter
            ivToolbarSearch.clickWithDebounce {
                findNavController().navigate(R.id.action_homeNewsFragment_to_searchNewsFragment)
            }
            ivToolbarSearch2.clickWithDebounce {
                findNavController().navigate(R.id.action_homeNewsFragment_to_searchNewsFragment)
            }
            adapter.setOnClickItem {
                (it as NewsModel?)?.let { d ->
                    val action =
                        HomeFragmentDirections.actionHomeNewsFragmentToDetailNewsFragment(d)
                    findNavController().navigate(action)
                }
            }
            binding.iError.btnRetry.clickWithDebounce {
                viewModel.getListHeadLine()
            }
        }
    }


    private fun setToolbar() {
        binding.apply {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    toolbar.visibility = View.VISIBLE
                } else {
                    toolbar.visibility = View.GONE
                }
            }
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.apply {
                    launch {
                        viewModel.loading.collectLatest {
                            binding.llcHeadLoading.isVisible = it
                            binding.llcHeader.isVisible = !it
                            adapter.setViewLoading(it)
                        }
                    }
                    launch {
                        viewModel.error.collectLatest {
                            binding.mainView.isVisible = it.isNullOrEmpty()
                            binding.iError.llcError.isVisible = !it.isNullOrEmpty()
                            binding.iError.tvError.text = if (!it.isNullOrEmpty()) it else ""
                        }
                    }
                    launch {
                        viewModel.response.collectLatest {
                            if (it.isNotEmpty()) {
                                binding.tvHead.text = it[0].title ?: ""
                                delay(1000)
                                adapter.differ.submitList(it)
                            }
                        }
                    }
                }
            }
        }
    }


}
