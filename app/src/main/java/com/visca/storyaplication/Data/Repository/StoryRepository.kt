package com.visca.storyaplication.Data.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.visca.storyaplication.Data.Configuration.ApiConfig
import com.visca.storyaplication.Data.Local.Room.ListStoryDatabase
import com.visca.storyaplication.Data.Preference.UserPreference
import com.visca.storyaplication.Data.Response.FileUploadResponse
import com.visca.storyaplication.Data.Response.ListStoryItem
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.Data.Response.Story
import com.visca.storyaplication.Data.Service.ApiService
import com.visca.storyaplication.Data.StoryRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor (private val storyDatabase: ListStoryDatabase, private val apiService: ApiService, private val userPreference: UserPreference,) {

    fun StoryList(): LiveData<List<ListStoryItem>> = liveData {
        emit(emptyList())
        val token = userPreference.getToken()
        val apiService = ApiConfig.getApiService(token.toString())
        try {
            val response = apiService.getStories()
            val stories = response.listStory
            if (!response.error) {
                emit(stories)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun listStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, userPreference),
            pagingSourceFactory = {
//                StoryPagingSource(userPreference)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }



//    get location story
    fun getStoriesWithLocation(location: Int): LiveData<List<ListStoryItem>> = liveData {
        emit(emptyList())
        val token = userPreference.getToken()
        val apiService = ApiConfig.getApiService(token.toString())
        try {
            val response = apiService.getStoriesWithLocation(location)
            val stories = response.listStory
            if (!response.error) {
                emit(stories)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

//    suspend fun getStoriesWithLocation(location: Int) : Flow<Response<List<ListStoryItem>>> = flow {
//        emit(Response.Loading)
//        val token = userPreference.getToken()
//        val apiService = ApiConfig.getApiService(token.toString())
//        try {
//            val response = apiService.getStoriesWithLocation(location)
//            val story = response.listStory
//            emit(Response.Success(story))
//        } catch (e: Exception) {
//            emit(Response.Error(e.message ?: "An error occurred"))
//        }
//    }


    suspend fun detailStory(id: String): Flow<Response<Story>> = flow {
        emit(Response.Loading)
        val token = userPreference.getToken()
        val apiService = ApiConfig.getApiService(token.toString())
        try {
            val response = apiService.getDetailStory(id)
            val story = response.story
            emit(Response.Success(story))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "An error occurred"))
        }
    }

    fun userStory(name: String): LiveData<Response<List<ListStoryItem>>> = liveData {
        emit(Response.Loading)
        val token = userPreference.getToken()
        val apiService = ApiConfig.getApiService(token.toString())
        try {
            val response = apiService.getStories()
            val result = response.listStory

            val filterName = result.filter { story ->
                story.name == name
            }

            emit(Response.Success(filterName))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "An error occurred"))
        }
    }

    suspend fun postStory(multipartBody: MultipartBody.Part, description: RequestBody): Flow<Response<FileUploadResponse>> = flow {
        emit(Response.Loading)
        val token = userPreference.getToken()
        val apiService = ApiConfig.getApiService(token.toString())
        try {
            val response = apiService.postStory(multipartBody, description)
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "An error occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            database: ListStoryDatabase ,
            apiService: ApiService ,
            userPreference: UserPreference ,
        ) = instance ?: synchronized(this) {
            instance ?: StoryRepository(database, apiService, userPreference)
        }.also { instance = it }
    }


}