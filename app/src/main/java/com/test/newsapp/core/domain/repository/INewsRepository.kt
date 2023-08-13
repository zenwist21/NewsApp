package com.test.newsapp.core.domain.repository

import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.core.data.model.Resource
import kotlinx.coroutines.flow.Flow

interface INewsRepository {
    fun getHeadlineNews(params: HashMap<String, Any>): Flow<Resource<List<NewsModel>>>

    fun getNews(params: HashMap<String, Any>): Flow<Resource<List<NewsModel>>>
}