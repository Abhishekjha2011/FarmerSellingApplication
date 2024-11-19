package com.example.farmerapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = UserDatabaseHelper(this)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()
            val address = editTextAddress.text.toString()


            if (isValidUsername(username) && isValidPassword(password)) {

                if (!dbHelper.checkUser(username)) {

                    dbHelper.addUser(username, password, phoneNumber, address)
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()


                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)


                    editTextUsername.text.clear()
                    editTextPassword.text.clear()
                    editTextPhoneNumber.text.clear()
                    editTextAddress.text.clear()
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid username or password format", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidUsername(username: String): Boolean {
        val regex = "^[a-zA-Z]{6}[0-9]{1}$".toRegex()
        return username.matches(regex)
    }


    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).*\$".toRegex()
        return password.matches(regex)
    }
}
