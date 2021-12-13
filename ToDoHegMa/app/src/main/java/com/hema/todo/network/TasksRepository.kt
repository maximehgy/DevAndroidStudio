package com.hema.todo.network

import com.hema.todo.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    suspend fun refresh() : List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body()!! else null
    }

    suspend fun create(task: Task) : Task?{
        val createResponse = tasksWebService.create(task)
        return if (createResponse.isSuccessful) createResponse.body() else null
    }

    suspend fun delete(task: Task) : Boolean {
        val deleteResponse = tasksWebService.delete(task.id)
        return task.id != null && deleteResponse.isSuccessful
    }


    suspend fun updateTask(task: Task) : Task? {
        val updateResponse = tasksWebService.update(task, task.id)
        return if (updateResponse.isSuccessful) updateResponse.body() else null
    }

}