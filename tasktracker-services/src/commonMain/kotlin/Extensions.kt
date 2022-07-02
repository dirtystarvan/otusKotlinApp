import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppStates

fun TrackerAppContext.successResponse(context: TrackerAppContext.() -> Unit) = apply(context).apply { status = TAppStates.RUNNING }

fun TrackerAppContext.errorResponse(buildError: () -> TAppError, error: (TAppError) -> TAppError) = apply {
    status = TAppStates.FAILING
    errors.add(error(buildError()))
}

val notFoundError: (String) -> String = { "Cannot find task with id [$it]" }