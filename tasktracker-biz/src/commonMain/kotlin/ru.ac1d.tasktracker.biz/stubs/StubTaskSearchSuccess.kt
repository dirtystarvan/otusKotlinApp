package ru.ac1d.tasktracker.biz.stubs

import BizTaskStub
import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.models.TAppTaskType
import ru.ac1d.tasktracker.common.stubs.TAppStubs

fun ICorChainDsl<TrackerAppContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == TAppStubs.SUCCESS && status == TAppStates.RUNNING }
    handle {
        status = TAppStates.FINISHING
        taskListResponse.addAll(BizTaskStub.prepareSearchList(taskFilterRequest.searchString, TAppTaskType.TASK))
    }
}
