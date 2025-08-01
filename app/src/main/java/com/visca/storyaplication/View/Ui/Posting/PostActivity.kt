package com.visca.storyaplication.View.Ui.Posting

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.R
import com.visca.storyaplication.Utils.reduceFileImage
import com.visca.storyaplication.Utils.uriToFile
import com.visca.storyaplication.View.Ui.MainActivity
import com.visca.storyaplication.View.Ui.Posting.CameraActivity.Companion.CAMERAX_RESULT
import com.visca.storyaplication.ViewModelFactory
import com.visca.storyaplication.databinding.ActivityPostBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PostActivity : AppCompatActivity(){

    private lateinit var binding: ActivityPostBinding
    private val viewModel by viewModels<PostViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    private val requestPermissLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, getString(R.string.permission_request_granted), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.permission_request_denied), Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }

        if (!allPermissionsGranted()) {
            requestPermissLauncher.launch(REQUIRED_PERMISSION)
        }


        viewModel.postResult.observe(this) { result ->
            when(result) {
                is Response.Loading -> {
                    binding.lineProgressBar.isIndeterminate = true
                    binding.lineProgressBar.visibility = View.VISIBLE
                    binding.overlayLoading.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }
                is Response.Success -> {
                    binding.lineProgressBar.visibility = View.GONE
                    binding.overlayLoading.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show()
                }
                is Response.Error -> {
                    binding.overlayLoading.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.lineProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Upload Gagal", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }

        binding.addImage.setOnClickListener {
            selectImage()
        }

        binding.btnPost.setOnClickListener { uploadImage() }


    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.descriptionInput.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val resquestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo", imageFile.name, resquestImageFile
            )

            viewModel.postStory(multipartBody, requestBody)
        }
    }

    private fun selectImage() {
        val optionActions = arrayOf<CharSequence>("Take Foto",
            "Your Galery", "Cancel")
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Take Foto From")
        dialogBuilder.setIcon(R.mipmap.ic_launcher)
        dialogBuilder.setItems(optionActions) { dialogInterface, i ->
            when(i) {
                0 -> {
                    startCameraX()
                }
                1 -> {
                    startGallery()
                }
                2 -> {
                    dialogInterface.dismiss()
                }
            }
        }
        dialogBuilder.show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI" , "showImage: $it")
            binding.addImage.setImageURI(it)
        }
        binding.tvAddPhoto.visibility = View.GONE
    }

    companion object {
        private  const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}