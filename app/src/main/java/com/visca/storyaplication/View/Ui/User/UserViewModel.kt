package com.visca.storyaplication.View.Ui.User

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.visca.storyaplication.Data.Repository.StoryRepository
import com.visca.storyaplication.Data.Response.ListStoryItem
import com.visca.storyaplication.Data.Response.Response

class UserViewModel (private val repository: StoryRepository): ViewModel() {

    fun getMyStory(name: String): LiveData<Response<List<ListStoryItem>>> {
        return repository.userStory(name)
    }
}