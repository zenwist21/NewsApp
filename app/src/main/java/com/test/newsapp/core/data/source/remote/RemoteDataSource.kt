package com.test.newsapp.core.data.source.remote

import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.core.data.model.Resource
import com.test.newsapp.core.data.model.toModel
import com.test.newsapp.core.data.source.remote.network.ApiService
import com.test.newsapp.core.domain.source.NewsDataSource
import com.test.newsapp.core.util.NetworkConnectivity
import com.test.newsapp.core.util.NetworkConstant.NETWORK_ERROR
import com.test.newsapp.core.util.NetworkConstant.NOT_FOUND
import com.test.newsapp.core.util.NetworkConstant.NO_INTERNET
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val networkConnectivity: NetworkConnectivity
) : NewsDataSource {
    override suspend fun getHeadlineList(params: HashMap<String, Any>): Resource<List<NewsModel>> {
        val responseCall = apiService::getHeadlineNews
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = NO_INTERNET)
        }
        return try {
            val response = responseCall.invoke(params)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = response.message()
                )
            }
            if (response.body()?.articles.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NOT_FOUND
                )
            }
            Resource.Success(
                data = response.body()?.articles?.toModel() ?: emptyList()
            )

        } catch (e: IOException) {
            return Resource.DataError(errorMessage = NETWORK_ERROR)
        }
    }

    override suspend fun getSearchNews(params: HashMap<String, Any>): Resource<List<NewsModel>> {
        val responseCall = apiService::getAllNews
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(errorMessage = NO_INTERNET)
        }
        return try {
            val response = responseCall.invoke(params)
            if (!response.isSuccessful) {
                return Resource.DataError(
                    errorMessage = response.message()
                )
            }
            if (response.body()?.articles.isNullOrEmpty()) {
                return Resource.DataError(
                    errorMessage = NOT_FOUND
                )
            }
            Resource.Success(
                data = response.body()?.articles?.toModel() ?: emptyList()
            )

        } catch (e: IOException) {
            return Resource.DataError(errorMessage = NETWORK_ERROR)
        }
    }
}