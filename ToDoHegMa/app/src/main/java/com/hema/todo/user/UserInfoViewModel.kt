package com.hema.todo.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel(){
    private val repository = UserInfoRepository()

    val _userInfo = MutableStateFlow<UserInfo?>(null)
    var userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()
    private val _LoginResponse = MutableStateFlow<LoginResponse?>(null)
    var LoginResponse = _LoginResponse.asStateFlow()



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

    fun login(user : LoginForm){
        viewModelScope.launch{
            val loginResult = repository.login(user)
            if (loginResult != null){
                _LoginResponse.value = loginResult
                LoginResponse = _LoginResponse
            }
        }
    }

    fun signup(user : SignupForm){
        viewModelScope.launch{
            val signupResult = repository.signup(user)
            if (signupResult != null){
                _LoginResponse.value = signupResult
                LoginResponse = _LoginResponse
            }
        }
    }
}