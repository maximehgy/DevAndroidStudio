package com.hema.todo.network

import com.hema.todo.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableStateFlow<List<Task>>(value = emptyList())

    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            if (fetchedTasks != null) _taskList.value = fetchedTasks
        }
    }

    suspend fun create(task: Task) {
        val tasksResponse = tasksWebService.create(task)
    }

    suspend fun delete(task: Task){
        val tasksResponse = tasksWebService.delete(task.id)
    }


    suspend fun updateTask(task: Task) {
        val updatedTask = tasksWebService.update(task, task.id)
        if (updatedTask.isSuccessful) {
            val oldTask = taskList.value.firstOrNull { it.id == updatedTask.body()?.id }
            if (oldTask != null && updatedTask.body() != null)
                _taskList.value = taskList.value - oldTask + updatedTask.body()!!
        }
    }

}