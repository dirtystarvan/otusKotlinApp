package ru.ac1d.tasktracker.common.repo

interface ITaskRepo {
    suspend fun createTask(request: DbTaskRequest): DbTaskResponse
    suspend fun readTask(request: DbTaskIdRequest): DbTaskResponse
    suspend fun updateTask(request: DbTaskRequest): DbTaskResponse
    suspend fun deleteTask(request: DbTaskIdRequest): DbTaskResponse
    suspend fun searchTask(request: DbTaskFilterRequest): DbTaskListResponse

    companion object {
        val NONE = object : ITaskRepo {
            override suspend fun createTask(request: DbTaskRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readTask(request: DbTaskIdRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateTask(request: DbTaskRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteTask(request: DbTaskIdRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchTask(request: DbTaskFilterRequest): DbTaskListResponse {
                TODO("Not yet implemented")
            }

        }
    }
}
