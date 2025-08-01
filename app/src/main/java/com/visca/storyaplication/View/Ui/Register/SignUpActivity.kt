package com.visca.storyaplication.View.Ui.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.R
import com.visca.storyaplication.View.Ui.Login.LoginActivity
import com.visca.storyaplication.ViewModelFactory
import com.visca.storyaplication.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {


        private lateinit var binding: ActivitySignUpBinding

        private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.UserResult.observe(this) { result ->
            when(result) {
                is Response.Loading -> {
                    binding.overlayLoading.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }
                is Response.Success -> {
                    binding.overlayLoading.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    Toast.makeText(this,
                        getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                is Response.Error -> {
                    binding.overlayLoading.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(this,
                        getString(R.string.email_is_already_taken), Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }

        binding.signupButton.setOnClickListener{
            val username = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.eTextPassword.editText?.text.toString()
            viewModel.register(username,email,password)
        }


    }
}