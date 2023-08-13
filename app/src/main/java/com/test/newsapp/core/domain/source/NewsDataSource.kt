package com.test.newsapp.core.domain.source

import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.core.data.model.Resource

interface NewsDataSource {
    suspend fun getHeadlineList(params: HashMap<String, Any>): Resource<List<NewsModel>>
    suspend fun getSearchNews(params: HashMap<String, Any>): Resource<List<NewsModel>>
}