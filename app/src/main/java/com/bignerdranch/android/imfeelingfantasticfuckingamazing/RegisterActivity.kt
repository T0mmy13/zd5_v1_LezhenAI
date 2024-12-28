package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import android.view.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Инициализация базы данных Room
        database = AppDatabase.getDatabase(this)
        userDao = database.userDao()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btnRegister: Button = findViewById(R.id.btnRegister)
        val etRegisterLogin: EditText = findViewById(R.id.etRegisterLogin)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etRegisterPassword: EditText = findViewById(R.id.etRegisterPassword)
        val etConfirmPassword: EditText = findViewById(R.id.etConfirmPassword)
        val cbTourAgent: CheckBox = findViewById(R.id.cbTourAgent)

        btnRegister.setOnClickListener {
            val login = etRegisterLogin.text.toString()
            val email = etEmail.text.toString()
            val password = etRegisterPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val isTourAgent = cbTourAgent.isChecked

            if (validateFields(login, email, password, confirmPassword)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = User(login = login, email = email, password = password, isTourAgent = isTourAgent)
                    userDao.insertUser(user)
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun validateFields(login: String, email: String, password: String, confirmPassword: String): Boolean {
        if (login.length < 4) {
            Toast.makeText(this, "Логин должен быть длиннее 3 символов", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!email.contains("gmail.com") && !email.contains("mail.ru") || email.indexOf('@') < 4) {
            Toast.makeText(this, "Некорректный email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Пароль должен быть длиннее 5 символов", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
