package ru.ac1d.tasktracker.common.repo

interface ITaskRepo {
    suspend fun createTask(request: DbTaskRequest): DbTaskResponse
    suspend fun readTask(request: DbTaskIdRequest): DbTaskResponse
    suspend fun updateTask(request: DbTaskRequest): DbTaskResponse
    suspend fun deleteTask(request: DbTaskIdRequest): DbTaskResponse
    suspend fun searchTask(request: DbTaskFilterRequest): DbTaskListResponse
}
