package com.ofcat.baseframe

import android.util.Log
import com.ofcat.baseframe.apiservice.HomeClientService
import com.ofcat.baseframe.scheduler.BaseSchedulers
import com.ofcat.baseframe.scheduler.DefaultSchedulers
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class MainPresenter(
    private val view: MainContract.View,
    private val schedulers: BaseSchedulers = DefaultSchedulers(),
    private val homeClientService: HomeClientService = HomeClientService.instance

) : MainContract.Presenter {

    private val TAG = MainPresenter::class.java.simpleName

    private var getDisposable: Disposable? = null

    override fun initial() {

        getDisposable = Observable
            // Dispose previous task
            .just(getDisposable?.dispose() ?: Unit)
            .subscribeOn(schedulers.io())

            // Show loading on subscribe
            .observeOn(schedulers.ui())
            .doOnSubscribe { view.showBaseLoadingDialog() }

            .observeOn(schedulers.io())
            .flatMap { homeClientService.getConfig() }

            // Hide loading on finally
            .observeOn(schedulers.ui())
            .doFinally { view.hideBaseLoadingDialog() }

            // Subscribe observable
            .observeOn(schedulers.ui())
            .subscribe({
                // do something you want
                view.showSearchPage()
            }, {
                // On error
                Log.e(TAG, it.message ?: "")
            })

    }

    override fun onSearchBarClick() {
    }

    override fun onLoginClick() {
    }

    override fun onDestroy() {
        getDisposable?.dispose()
    }
}