package ru.ac1d.tasktracker.biz

import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import ru.ac1d.tasktracker.biz.general.initStatus
import ru.ac1d.tasktracker.biz.general.operation
import ru.ac1d.tasktracker.biz.general.stubs
import ru.ac1d.tasktracker.biz.general.validation
import ru.ac1d.tasktracker.biz.stubs.*
import ru.ac1d.tasktracker.biz.validation.*
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppCommand
import ru.ac1d.tasktracker.common.models.TAppTaskId

class TAppTaskProcessor {
    suspend fun exec(context: TrackerAppContext) = BusinessChain.exec(context)

    companion object {
        private val BusinessChain = rootChain<TrackerAppContext> {
            initStatus("Context status init")

            operation("Create task", TAppCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation("Валидация запроса") {
                    worker("Копируем поля в taskValidating") { taskValidating = taskRequest.deepCopy() }
                    worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                    worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }

                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
            }

            operation("Read task", TAppCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation("Валидация запроса") {
                    worker("Копируем поля в taskValidating") { taskValidating = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TAppTaskId(taskValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
            }

            operation("Update task", TAppCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation("Валидация запроса") {
                    worker("Копируем поля в taskValidating") { taskValidating = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TAppTaskId(taskValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                    worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
            }

            operation("Delete task", TAppCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation("Валидация запроса") {
                    worker("Копируем поля в taskValidating") { taskValidating = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TAppTaskId(taskValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
            }

            operation("Search task", TAppCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation("Валидация запроса") {
                    worker("Копируем поля в taskFilterValidating") { taskFilterValidating = taskFilterRequest.copy() }

                    finishTaskFilterValidation("Успешное завершение процедуры валидации")
                }
            }
        }.build()
    }
}
