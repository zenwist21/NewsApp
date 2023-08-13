package com.test.newsapp.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.test.newsapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date

const val RECYCLER_VIEW_SUCCESS = 200
const val RECYCLER_VIEW_LOADING = 0
const val RECYCLER_VIEW_LOADING_NEXT = 10
const val RECYCLER_VIEW_ERROR = 500

const val DUMMY: String = "THIS_IS_DUMMY_DATA_ONLY"

//INTENT KEY
const val itemID = "ITEM_ID"
const val itemTYPE = "itemTYPE"

fun ImageView.loadImage(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_dummy_background)
        .error(R.color.colorSoftGrey)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontAnimate()
        .thumbnail(
            Glide.with(context)
                .load(R.drawable.ic_dummy_background)
        )
        .into(this)

}

@SuppressLint("SimpleDateFormat")
fun convertDateFormat(
    current: String,
    oldFormat: String, newFormat: String
): String {
    return try {
        var dateFormat = SimpleDateFormat(oldFormat)
        val newDate = dateFormat.parse(current)
        dateFormat = SimpleDateFormat(newFormat)
        dateFormat.format(newDate ?: Date())
    } catch (e: Exception) {
        ""
    }

}

fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}


@ExperimentalCoroutinesApi
fun <T, K> StateFlow<T>.mapState(
    scope: CoroutineScope,
    initialValue: K,
    transform: suspend (data: T) -> K
): StateFlow<K> {
    return mapLatest {
        transform(it)
    }
        .stateIn(scope, SharingStarted.Eagerly, initialValue)
}

