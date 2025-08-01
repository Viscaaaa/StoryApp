package com.visca.storyaplication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.visca.storyaplication.Data.Repository.RepositoryUser
import com.visca.storyaplication.Data.Repository.StoryRepository
import com.visca.storyaplication.View.Ui.List.StoryViewModel
import com.visca.storyaplication.View.Ui.Login.LoginViewModel
import com.visca.storyaplication.View.Ui.MainViewModel
import com.visca.storyaplication.View.Ui.MapsViewModel
import com.visca.storyaplication.View.Ui.Posting.PostViewModel
import com.visca.storyaplication.View.Ui.Register.SignUpViewModel
import com.visca.storyaplication.View.Ui.User.UserViewModel
import com.visca.storyaplication.di.Injection

class ViewModelFactory (
    private val repository: RepositoryUser,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(context) ,
                    Injection.storyRepository(context)
                )
            }.also { INSTANCE = it }
    }
}