package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTourActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var tourDao: TourDao
    private lateinit var userDao: UserDao
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tour)

        // Устанавливаем Toolbar как ActionBar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Устанавливаем пустой заголовок, включаем кнопку возврата
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Инициализация базы данных Room
        database = AppDatabase.getDatabase(this)
        tourDao = database.tourDao()
        userDao = database.userDao()

        CoroutineScope(Dispatchers.IO).launch {
            currentUser = userDao.getCurrentLoggedInUser()!!
        }

        val etTourName: EditText = findViewById(R.id.etTourName)
        val etCountry: EditText = findViewById(R.id.etCountry)
        val etDescription: EditText = findViewById(R.id.etDescription)
        val etCost: EditText = findViewById(R.id.etCost)
        val btnAddTour: Button = findViewById(R.id.btnAddTour)

        btnAddTour.setOnClickListener {
            val name = etTourName.text.toString()
            val country = etCountry.text.toString()
            val description = etDescription.text.toString()
            val cost = etCost.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && country.isNotEmpty() && description.isNotEmpty() && cost != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    // Передаем registeredByAgentId при создании Tour
                    val tour = Tour(
                        name = name,
                        country = country,
                        description = description,
                        cost = cost,
                        registeredByAgentId = currentUser.id // Здесь передаем ID текущего агента
                    )
                    tourDao.insertTour(tour)
                    runOnUiThread {
                        Toast.makeText(this@AddTourActivity, "Тур добавлен успешно", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля корректно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Возвращаемся на экран MainActivity
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}