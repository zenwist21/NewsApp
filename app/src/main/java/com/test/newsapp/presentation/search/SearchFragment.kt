package com.test.newsapp.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.databinding.FragmentSearchBinding
import com.test.newsapp.presentation.base.BaseFragment
import com.test.newsapp.presentation.util.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private val adapter: SearchAdapter by lazy { SearchAdapter() }
    private var timer: Timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initController()
        return binding.root
    }

    override fun initEvent() {
        binding.apply {
            rvSearch.adapter = adapter
            ivBack.clickWithDebounce {
                if (edtSearch.text.isNullOrEmpty()) {
                    findNavController().navigateUp()
                } else {
                    edtSearch.setText(null)
                }
            }
            edtSearch.doAfterTextChanged back@{
                if (it.isNullOrEmpty()) {
                    lblCenter.isVisible = true
                    rvSearch.isVisible = false
                    return@back
                }
                lblCenter.isVisible = false
                rvSearch.isVisible = true
                doSearch(it.toString())
            }
            adapter.setOnClickItem {
                (it as NewsModel?)?.let { d ->
                    val action =
                        SearchFragmentDirections.actionSearchNewsFragmentToDetailNewsFragment(d)
                    findNavController().navigate(action)
                }
            }
            iError.btnRetry.clickWithDebounce {
                if (!edtSearch.text.isNullOrEmpty()) viewModel.getData(edtSearch.text.toString())
                else viewModel.clearError()
            }
        }
    }

    private fun doSearch(query: String) {
        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewModel.getData(query)
            }
        }, 1000)
    }

    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.apply {
                    launch {
                        viewModel.loading.collectLatest {
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
                            delay(1000)
                            adapter.differ.submitList(it)
                        }
                    }
                }
            }
        }
    }


}