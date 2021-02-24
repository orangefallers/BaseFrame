package com.ofcat.baseframe.scheduler

import io.reactivex.Scheduler

interface BaseSchedulers {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}