package com.visca.storyaplication.View.Ui.Posting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visca.storyaplication.Data.Repository.StoryRepository
import com.visca.storyaplication.Data.Response.FileUploadResponse
import com.visca.storyaplication.Data.Response.Response
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostViewModel (private val repository: StoryRepository): ViewModel() {

    private val _postResult = MutableLiveData<Response<FileUploadResponse>>()
    val postResult: LiveData<Response<FileUploadResponse>> = _postResult

    fun postStory(multipartBody: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            repository.postStory(multipartBody, description).collect { result ->
                _postResult.value = result
            }
        }
    }
}