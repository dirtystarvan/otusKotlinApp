package ru.ac1d.tasktracker.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.ac1d.tasktracker.biz.TAppTaskProcessor
import ru.ac1d.tasktracker.common.models.TAppCommand
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ValidationUpdateTest {
    private val processor = TAppTaskProcessor()
    private val command = TAppCommand.UPDATE

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)
    @Test
    fun trimTitle() = validationTitleTrim(command, processor)
    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test
    fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test
    fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test
    fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test
    fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test
    fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)
}
