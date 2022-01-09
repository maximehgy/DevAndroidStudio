package com.hema.todo.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hema.todo.network.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    public val taskList: StateFlow<List<Task>> = _taskList

    fun refresh() {
        viewModelScope.launch {
            val tasks = repository.refresh()
            if (tasks != null) {
                _taskList.value = tasks
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch{
            if (repository.delete(task)){
                _taskList.value.toMutableList().remove(task)
            }
            refresh()
        }
    }

    fun add(task: Task) {
        viewModelScope.launch{
            val task = repository.create(task)
            if (task!= null){
                _taskList.value.toMutableList().add(task)
            }
            refresh()
        }
    }
    fun edit(task : Task){
        viewModelScope.launch{
            val task = repository.updateTask(task)
            if (task!=null){
                val list = _taskList.value.toMutableList()
                val pos = list.indexOfFirst { it.id == task.id }
                list.set(pos, task)
            }
            refresh()
        }
    }
}