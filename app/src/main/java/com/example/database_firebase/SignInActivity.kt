package com.example.database_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    lateinit var databaseReference: DatabaseReference

    companion object {
        const val KEY1 = "com.example.database_firebase.SignInActivity.mail"
        const val KEY2 = "com.example.database_firebase.SignInActivity.name"
        const val KEY3 = "com.example.database_firebase.SignInActivity.id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val username = findViewById<TextInputEditText>(R.id.userName)

        btnSignIn.setOnClickListener {

            val uniqueId = username.text.toString()

            if(uniqueId.isNotEmpty()) {
                readData(uniqueId)
            } else {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readData(uniqueId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(uniqueId).get().addOnSuccessListener {

            if(it.exists()) {
                // welcome user to your app with intent and pass data
                val email = it.child("email").value
                val name = it.child("name").value
                val userId = it.child("uniqueId").value

                val intentWelcome = Intent(this, WelcomeActivity::class.java)
                intentWelcome.putExtra(KEY1, email.toString())
                intentWelcome.putExtra(KEY2, name.toString())
                intentWelcome.putExtra(KEY3, uniqueId.toString())
                startActivity(intentWelcome)

            } else {
                Toast.makeText(this, "User with this id does not exists!", Toast.LENGTH_LONG).show()
            }

            // In case of db or network failure
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
        }
    }
}