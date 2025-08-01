package com.visca.storyaplication.View.Ui.DetailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.visca.storyaplication.Data.Preference.Model
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.R
import com.visca.storyaplication.Utils.isInternetAvailable
import com.visca.storyaplication.View.Ui.List.StoryViewModel
import com.visca.storyaplication.View.Ui.Login.LoginViewModel
import com.visca.storyaplication.ViewModelFactory
import com.visca.storyaplication.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedId = intent.getStringExtra("idUser").toString()
        val receivedName = intent.getStringExtra("name").toString()
        val receivedDesc = intent.getStringExtra("desc").toString()
        val receivedPhoto = intent.getStringExtra("photo").toString()
        viewModel.detailStory(receivedId)


        viewModel.detailResult.observe(this) { response ->
            when(response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    setDetailStory(
                        response.data.name ,
                        response.data.photoUrl ,
                        response.data.description
                    )
                }
                is Response.Error -> {
                    if (!isInternetAvailable(this)) {
                        setDetailStory(receivedName, receivedPhoto, receivedDesc)
                    }
                    Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        binding.fabDetail.setOnClickListener {
            finish()
        }
    }

    private fun setDetailStory(name: String, img: String, desc: String) {
        binding.tvNameDetail.text = name
        binding.tvDescDetail.text = desc
        Glide.with(applicationContext)
            .load(img)
            .into(binding.ivDetail)

    }
}