package br.com.wakim.github.data

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.internal.schedulers.ImmediateThinScheduler

interface SchedulerProviderContract {
    val io: Scheduler
    val ui: Scheduler
    val computation: Scheduler
}

class SchedulerProvider : SchedulerProviderContract {
    override val io: Scheduler
        get() = Schedulers.io()

    override val ui: Scheduler
        get() = AndroidSchedulers.mainThread()

    override val computation: Scheduler
        get() = Schedulers.computation()
}

class ImmediateSchedulerProvider : SchedulerProviderContract {
    override val io: Scheduler
        get() = ImmediateThinScheduler.INSTANCE

    override val ui: Scheduler
        get() = ImmediateThinScheduler.INSTANCE

    override val computation: Scheduler
        get() = ImmediateThinScheduler.INSTANCE
}