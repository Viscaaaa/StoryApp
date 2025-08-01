package com.visca.storyaplication.View.Ui.List

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.visca.storyaplication.Data.Repository.StoryRepository
import com.visca.storyaplication.Data.Response.ListStoryItem
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.Data.Response.Story
import kotlinx.coroutines.launch

class StoryViewModel (private val repository : StoryRepository) : ViewModel () {

    fun storyList(): LiveData<List<ListStoryItem>> = repository.StoryList()

    val listStory: LiveData<PagingData<ListStoryItem>> = repository.listStory().cachedIn(viewModelScope)

    private val _detailResult = MutableLiveData<Response<Story>>()
    val detailResult: LiveData<Response<Story>> = _detailResult

    fun detailStory(id: String) {
        viewModelScope.launch {
            repository.detailStory(id).collect { result ->
                _detailResult.value = result
            }
        }
    }

}