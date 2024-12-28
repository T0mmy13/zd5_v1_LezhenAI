package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Инициализация базы данных Room
        database = AppDatabase.getDatabase(this)
        userDao = database.userDao()

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val etLogin: EditText = findViewById(R.id.etLogin)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val tvRegister: TextView = findViewById(R.id.tvRegister)

        // Проверка на текущего пользователя, если он уже залогинен
        CoroutineScope(Dispatchers.IO).launch {
            val currentUser = userDao.getCurrentLoggedInUser()
            if (currentUser != null) {
                // Автоматически войти и перейти на MainActivity
                runOnUiThread {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        putExtra("USER_LOGIN", currentUser.login)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString()
            val password = etPassword.text.toString()
            performLogin(login, password)
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(login: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserByLogin(login)
            runOnUiThread {
                if (user != null && user.password == password) {
                    Toast.makeText(this@LoginActivity, "Вход успешен", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.IO).launch {
                        userDao.clearCurrentLoggedInUser()
                        userDao.setCurrentLoggedInUser(login)
                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        putExtra("USER_LOGIN", login)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Неверные логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}