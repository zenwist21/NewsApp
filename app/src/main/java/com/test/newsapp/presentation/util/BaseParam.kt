package com.test.newsapp.presentation.util

import com.test.newsapp.BuildConfig

fun getBaseParam(
    search: String? = null,
    country:String = "us"
): HashMap<String, Any> {
    val params: HashMap<String, Any> = HashMap()
    params["apiKey"] = BuildConfig.ACCESS_TOKEN
    if (search.isNullOrEmpty()) params["country"] = country
    else params["q"] = search
    return params
}