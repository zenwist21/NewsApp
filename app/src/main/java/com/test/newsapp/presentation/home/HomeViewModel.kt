package com.test.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.newsapp.core.data.model.NewsModel
import com.test.newsapp.core.data.model.Resource
import com.test.newsapp.core.domain.repository.INewsRepository
import com.test.newsapp.core.util.NetworkConstant.NO_INTERNET
import com.test.newsapp.presentation.util.getBaseParam
import com.test.newsapp.presentation.util.mapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: INewsRepository
) : ViewModel() {
    private var _state = MutableStateFlow(HomeUIState())

    val loading: StateFlow<Boolean> =
        _state.mapState(viewModelScope, initialValue = false) { it.loading }
    val error: StateFlow<String?> =
        _state.mapState(viewModelScope, initialValue = null) { it.error }
    val response: StateFlow<List<NewsModel>> =
        _state.mapState(viewModelScope, initialValue = emptyList()) { it.response }

    init {
        getListHeadLine()
    }

     fun getListHeadLine() {
        viewModelScope.launch {
            repository.getHeadlineNews(getBaseParam()).onEach {
                when (it) {
                    is Resource.DataError -> {
                        _state.update { state ->
                            state.copy(
                                loading = false,
                                error = it.errorMessage?.toString() ?: NO_INTERNET
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { state -> state.copy(loading = true, error = null) }
                    }

                    is Resource.Success -> {
                        _state.update { state ->
                            state.copy(
                                loading = false,
                                response = it.data ?: emptyList()
                            )
                        }
                    }
                }
            }.launchIn(this)
        }
    }

}

data class HomeUIState(
    val loading: Boolean = false,
    val response: List<NewsModel> = emptyList(),
    val error: String? = null
)