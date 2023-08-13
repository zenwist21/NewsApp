package com.test.newsapp.core.data.model

import android.os.Parcelable
import com.test.newsapp.core.data.source.remote.response.NewsResponse
import com.test.newsapp.core.data.source.remote.response.SourceResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsModel(
    val source: SourceModel? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
) : Parcelable

@Parcelize
data class SourceModel(
    val id: String? = null,
    val name: String? = null
) : Parcelable

fun SourceResponse.toModel(): SourceModel = SourceModel(
    id, name
)

fun List<NewsResponse>.toModel(): List<NewsModel> {
    val temp = mutableListOf<NewsModel>()
    this.forEach {
        temp.add(
            NewsModel(
                it.source?.toModel(),
                it.author,
                it.title,
                it.description,
                it.url,
                it.urlToImage,
                it.publishedAt,
                it.content
            )
        )
    }
    return temp
}