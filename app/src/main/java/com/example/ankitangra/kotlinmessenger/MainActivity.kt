package com.example.ankitangra.kotlinmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_registration.setOnClickListener({
            val email = email_edittext_registration.text.toString()
            val password = password_edittext_registration.text.toString()

            Log.d("MainActivity","Email is : " + email )
            Log.d("MainActivity","Email is : " + password )
        })

        already_have_account_textview_registration.setOnClickListener {
            Log.d("MainActivity","Try to show login activity")

            // Launch Login Activity
            val intent = Intent (this,LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
