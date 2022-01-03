package com.hema.todo.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hema.todo.network.TasksRepository
import com.hema.todo.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel(){
    private val repository = UserInfoRepository()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    public val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _userInfo.value = repository.refresh()
        }
    }


    fun editAvatar( avatar : MultipartBody.Part) {
        viewModelScope.launch{
            val result = repository.updateAvatar(avatar)
            if (result!= null){
                _userInfo.value = result
            }
        }
    }

    fun editUser(user : UserInfo){
        viewModelScope.launch{
            val UserResult = repository.updateUser(user)
            if (UserResult!=null){
                _userInfo.value = UserResult
            }
        }
    }
}