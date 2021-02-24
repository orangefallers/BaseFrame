package com.ofcat.baseframe.scheduler


import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class ImmediateSchedulers : BaseSchedulers {

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}