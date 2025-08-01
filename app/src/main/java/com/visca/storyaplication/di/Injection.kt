package com.visca.storyaplication.di

import android.content.Context
import com.visca.storyaplication.Data.Configuration.ApiConfig
import com.visca.storyaplication.Data.Local.Room.ListStoryDatabase
import com.visca.storyaplication.Data.Preference.UserPreference
import com.visca.storyaplication.Data.Preference.dataStore
import com.visca.storyaplication.Data.Repository.RepositoryUser
import com.visca.storyaplication.Data.Repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context):RepositoryUser {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val userPreference = UserPreference.getInstance(context.dataStore)
        return RepositoryUser.getInstance(apiService, userPreference)
    }

    fun storyRepository(context: Context): StoryRepository {
        val database = ListStoryDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(database, apiService, pref)
    }
}