package com.visca.storyaplication.View.Ui.Register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visca.storyaplication.Data.Repository.RepositoryUser
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.Data.Response.UserResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: RepositoryUser) : ViewModel() {


    private val _UserResult = MutableLiveData<Response<UserResponse>>()
    val UserResult: LiveData<Response<UserResponse>> = _UserResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.register(name, email, password).collect{ result ->
                _UserResult.value = result

            }
        }
    }

}