package com.ofcat.baseframe.tool

import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.annotation.NonNull
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

@Suppress("UNUSED")
object RxBindingTools {

    fun onClicks(@NonNull view: View): Observable<Any> {
        return RxView.clicks(view)
            .throttleFirst(60, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun onChanges(@NonNull view: TextView): Observable<String> {
        return RxTextView.textChanges(view)
            .skip(1)
            .debounce(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.toString() }
    }

    fun onChecks(@NonNull view: CompoundButton): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(view)
            .throttleFirst(60, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }
}