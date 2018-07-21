package com.example.ankitangra.kotlinmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_registration.setOnClickListener({
            val email = email_edittext_registration.text.toString()
            val password = password_edittext_registration.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        // else if successful
                        Log.d("Main","Successfully Created User with uid : ${it.result.user.uid}")
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
        })

        already_have_account_textview_registration.setOnClickListener {
            Log.d("MainActivity","Try to show login activity")
            Log.i("MainActivity","Try to show login activity")

            val intent = Intent (this,LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
