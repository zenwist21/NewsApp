package com.test.newsapp.core.data.source.remote.network

import com.test.newsapp.core.data.source.remote.response.BaseResponse
import com.test.newsapp.core.data.source.remote.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("top-headlines")
    suspend fun getHeadlineNews(@QueryMap hashMap: HashMap<String, Any>): Response<BaseResponse<List<NewsResponse>>>


    // Search
    @GET("everything")
    suspend fun getAllNews(@QueryMap hashMap: HashMap<String, Any>): Response<BaseResponse<List<NewsResponse>>>

}