package ru.ac1d.tasktracker.biz.stubs

import BizTaskStub
import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.models.TAppTaskId
import ru.ac1d.tasktracker.common.models.TAppTaskStatus
import ru.ac1d.tasktracker.common.models.TAppTaskType
import ru.ac1d.tasktracker.common.stubs.TAppStubs

fun ICorChainDsl<TrackerAppContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == TAppStubs.SUCCESS && status == TAppStates.RUNNING }
    handle {
        status = TAppStates.FINISHING
        taskResponse = BizTaskStub.prepareResult {
            taskRequest.id.takeIf { it != TAppTaskId.NONE }?.also { this.id = it }
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            taskRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            taskRequest.type.takeIf { it != TAppTaskType.NONE }?.also { this.type = it }
            taskRequest.reporterId.takeIf { it.asString().isNotBlank() }?.also { this.reporterId = it }
            taskRequest.executorId.takeIf { it.asString().isNotBlank() }?.also { this.executorId = it }
            taskRequest.status.takeIf { it != TAppTaskStatus.NONE }?.also { this.status = it }
        }
    }
}
