package com.visca.storyaplication.Data.Service

import com.visca.storyaplication.Data.Response.DetailStoryResponse
import com.visca.storyaplication.Data.Response.FileUploadResponse
import com.visca.storyaplication.Data.Response.LoginResponse
import com.visca.storyaplication.Data.Response.StoryResponse
import com.visca.storyaplication.Data.Response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") string: String
    ): UserResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") string: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): StoryResponse

//untuk paging
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int ,
        @Query("size") size: Int
    ): StoryResponse

//    untuk get location
    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse


        @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): FileUploadResponse



}