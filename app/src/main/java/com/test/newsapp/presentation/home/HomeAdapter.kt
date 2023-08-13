package com.test.newsapp.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.newsapp.R
import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.databinding.ListItemHomeBinding
import com.test.newsapp.databinding.ListItemHomeSkeletonBinding
import com.test.newsapp.presentation.util.RECYCLER_VIEW_LOADING
import com.test.newsapp.presentation.util.RECYCLER_VIEW_SUCCESS
import com.test.newsapp.presentation.util.clickWithDebounce
import com.test.newsapp.presentation.util.loadImage

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClick: ((Any?) -> Unit)? = null
    private var isLoading = false
    private val diffCallBack = object : DiffUtil.ItemCallback<NewsModel>() {
        override fun areItemsTheSame(
            oldItem: NewsModel, newItem: NewsModel
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: NewsModel, newItem: NewsModel
        ): Boolean = oldItem == newItem

        override fun getChangePayload(
            oldItem: NewsModel, newItem: NewsModel
        ): Any {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    inner class Loading(
        private val binding: ListItemHomeSkeletonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                shimmerLayout.startShimmer()
            }
        }
    }


    inner class Item(val parent: ViewGroup, private val binding: ListItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CheckResult")
        fun bind(data: NewsModel) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    root.clickWithDebounce {
                        onItemClick?.invoke(data)
                    }
                    tvNews.text = data.title
                    if (!data.urlToImage.isNullOrEmpty()) ivNews.loadImage(
                        ctx, data.urlToImage ?: ""
                    )
                    else ivNews.setImageDrawable(
                        ContextCompat.getDrawable(
                            ctx, R.drawable.dummy_news
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            RECYCLER_VIEW_LOADING -> {
                return Loading(
                    ListItemHomeSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            else -> {
                return Item(
                    parent, ListItemHomeBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) {
            RECYCLER_VIEW_LOADING
        } else {
            RECYCLER_VIEW_SUCCESS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is Item -> {
                holder.bind(differ.currentList[position])
            }

            is Loading -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
    fun setViewLoading(state: Boolean) {
        isLoading = state
        if (isLoading) {
            this.differ.submitList(
                listOf(NewsModel(), NewsModel(), NewsModel(), NewsModel())
            )
        }

    }

    fun setOnClickItem(listener: (Any?) -> Unit) {
        onItemClick = listener
    }
}