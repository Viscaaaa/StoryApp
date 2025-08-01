package com.visca.storyaplication.View.Ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visca.storyaplication.Data.Repository.StoryRepository
import com.visca.storyaplication.Data.Response.ListStoryItem
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.Data.Response.Story
import com.visca.storyaplication.Data.Response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository): ViewModel()  {


    fun getStoriesWithLocation(location: Int): LiveData<List<ListStoryItem>> {
        return repository.getStoriesWithLocation(location)
    }

}