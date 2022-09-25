package ru.ac1d.tasktracker.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppCommand
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.models.TAppWorkMode

fun ICorChainDsl<TrackerAppContext>.initStatus(title: String) = worker() {
    this.title = title
    on { status == TAppStates.NONE }
    handle { status = TAppStates.RUNNING }
}

fun ICorChainDsl<TrackerAppContext>.operation(title: String, command: TAppCommand, block: ICorChainDsl<TrackerAppContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && status == TAppStates.RUNNING }
}

fun ICorChainDsl<TrackerAppContext>.validation(title: String, block: ICorChainDsl<TrackerAppContext>.() -> Unit) = chain {
    block()
    this.title = title
}

fun ICorChainDsl<TrackerAppContext>.stubs(title: String, block: ICorChainDsl<TrackerAppContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == TAppWorkMode.STUB && status == TAppStates.RUNNING }
}
