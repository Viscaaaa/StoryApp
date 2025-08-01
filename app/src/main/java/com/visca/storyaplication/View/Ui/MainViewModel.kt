package com.visca.storyaplication.View.Ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.visca.storyaplication.Data.Preference.Model
import com.visca.storyaplication.Data.Repository.RepositoryUser

class MainViewModel (private val repository: RepositoryUser): ViewModel(){
    fun getSession(): LiveData<Model> {
        return repository.getSession().asLiveData()
    }
}