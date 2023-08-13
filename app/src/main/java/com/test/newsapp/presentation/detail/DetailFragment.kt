package com.test.newsapp.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.test.newsapp.R
import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.databinding.FragmentDetailBinding
import com.test.newsapp.presentation.base.BaseFragment
import com.test.newsapp.presentation.util.clickWithDebounce
import com.test.newsapp.presentation.util.convertDateFormat
import com.test.newsapp.presentation.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : BaseFragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var data: NewsModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        initController {
            setUI()
        }
        return binding.root
    }

    private fun setUI() {
        binding.apply {
            DetailFragmentArgs.fromBundle(requireArguments()).let {
                data = it.newsData
            }
            data?.let { data ->
                tvTitle.text = data.title ?: ""
                tvContent.text = data.content ?: ""
                tvSource.text = getString(
                    R.string.source_value,
                    data.author ?: "Media",
                    data.source?.name ?: "-"
                )
                tvPublishedAt.text = getString(
                    R.string.date_value,
                    convertDateFormat(
                        data.publishedAt ?: "",
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        "dd MMM yyyy"
                    )
                )
                if (!data.urlToImage.isNullOrEmpty()) ivHeader.loadImage(
                    requireContext(), data.urlToImage
                )
                else ivHeader.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.dummy_news
                    )
                )
            }
        }
    }

    override fun initEvent() {
        binding.apply {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            ivBack.clickWithDebounce {
                findNavController().navigateUp()
            }

        }
    }


    override fun initObserver() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        data = null
    }
}
