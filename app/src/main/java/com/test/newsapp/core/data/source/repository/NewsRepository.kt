package com.test.newsapp.core.data.source.repository

import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.core.data.model.Resource
import com.test.newsapp.core.data.source.remote.RemoteDataSource
import com.test.newsapp.core.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NewsRepository @Inject constructor(
    private val remoteData: RemoteDataSource,
    private val ioDispatcher: CoroutineContext
) : INewsRepository {
    override fun getHeadlineNews(params: HashMap<String, Any>): Flow<Resource<List<NewsModel>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getHeadlineList(params))
        }.flowOn(ioDispatcher)
    }

    override fun getNews(params: HashMap<String, Any>): Flow<Resource<List<NewsModel>>> {
        return flow {
            emit(Resource.Loading())
            emit(remoteData.getSearchNews(params))
        }.flowOn(ioDispatcher)
    }
}