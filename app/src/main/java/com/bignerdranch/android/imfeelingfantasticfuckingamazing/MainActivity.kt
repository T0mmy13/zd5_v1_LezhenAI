package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TourUpdateListener {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getDatabase(this)
        userDao = database.userDao()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val userLogin = intent.getStringExtra("USER_LOGIN")

        val tvGreeting: TextView = findViewById(R.id.tvGreeting)
        val btnAddTour: Button = findViewById(R.id.btnAddTour)
        val btnEditTour: Button = findViewById(R.id.btnEditTour)
        val btnDeleteTour: Button = findViewById(R.id.btnDeleteTour)

        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserByLogin(userLogin ?: "")
            runOnUiThread {
                if (user != null) {
                    tvGreeting.text = "Здравствуйте, ${user.login}"
                    Log.d("MainActivity", "Retrieved user: $user")

                    if (user.isTourAgent) {
                        btnAddTour.visibility = android.view.View.VISIBLE
                        btnEditTour.visibility = android.view.View.VISIBLE
                        btnDeleteTour.visibility = android.view.View.VISIBLE
                    }
                }
            }
        }

        btnAddTour.setOnClickListener {
            val intent = Intent(this, AddTourActivity::class.java)
            startActivity(intent)
        }

        btnDeleteTour.setOnClickListener {
            val intent = Intent(this, DeleteTourActivity::class.java)
            startActivity(intent)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Profile -> {
                    Log.d("MainActivity", "Profile selected")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TourListFragment.newInstance(true))
                        .commit()
                    true
                }
                R.id.Tour -> {
                    Log.d("MainActivity", "Tour selected")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TourListFragment.newInstance(false))
                        .commit()
                    true
                }
                R.id.exit -> {
                    Log.d("MainActivity", "Exit selected")
                    CoroutineScope(Dispatchers.IO).launch {
                        userDao.clearCurrentLoggedInUser()
                    }
                    val intent = Intent(this@MainActivity, LoginActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onTourListUpdated() {
        reloadProfile()
    }

    private fun reloadProfile() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TourListFragment.newInstance(true))
            .commit()
    }
}