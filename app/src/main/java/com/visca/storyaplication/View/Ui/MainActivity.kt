package com.visca.storyaplication.View.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.visca.storyaplication.R
import com.visca.storyaplication.View.Ui.Login.LoginActivity
import com.visca.storyaplication.View.Ui.Posting.PostActivity
import com.visca.storyaplication.ViewModelFactory
import com.visca.storyaplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var nameUser = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val toolbar = binding.toolbar
//        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
        }

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val fragmentTitle = when (destination.id) {
                R.id.userFragment -> nameUser
                R.id.listFragment -> getString(R.string.list_story)
                else -> getString(R.string.app_name)
            }

            supportActionBar?.title = fragmentTitle
        }

        mainViewModel.getSession().observe(this) { user ->
            nameUser = user.username
            if (user?.isLogin == false) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        goToMap()


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.postActivity ->  {
                startActivity(Intent(this, PostActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    fun goToMap (){
        binding.AppBar.setOnMenuItemClickListener{ menuItem ->
            when(menuItem.itemId) {
                R.id.map -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

}