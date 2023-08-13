package com.test.newsapp.presentation.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    fun initController(unit:(()-> Unit)? = null) {
        unit?.invoke()
        initEvent()
        initObserver()
    }

    abstract fun initEvent()
    abstract fun initObserver()

}