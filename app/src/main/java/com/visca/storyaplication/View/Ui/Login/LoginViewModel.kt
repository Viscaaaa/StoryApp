package com.visca.storyaplication.View.Ui.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visca.storyaplication.Data.Preference.Model
import com.visca.storyaplication.Data.Repository.RepositoryUser
import com.visca.storyaplication.Data.Response.LoginResponse
import com.visca.storyaplication.Data.Response.Response
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: RepositoryUser): ViewModel() {

    private val _loginResult = MutableLiveData<Response<LoginResponse>>()
    val loginResult: LiveData<Response<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                _loginResult.value = result
            }
        }
    }

    fun saveSession(user: Model) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<Model> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}