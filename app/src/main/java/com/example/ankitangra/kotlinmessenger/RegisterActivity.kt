package com.example.ankitangra.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        selectphoto_button_register.setOnClickListener {

            val intent = Intent (Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

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
                        uploadImageToFirebaseStore ()

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
        })

        already_have_account_textview_registration.setOnClickListener {
            Log.d("RegisterActivity","Try to show login activity")
            Log.i("RegisterActivity","Try to show login activity")

            val intent = Intent (this,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private  fun uploadImageToFirebaseStore () {

        if (selectedPhotoUrl == null)  {
            saveDataToFirebaseDatabase("")
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUrl!!).addOnSuccessListener {

            ref.downloadUrl
                    .addOnSuccessListener {
                        it.toString()
                        saveDataToFirebaseDatabase(it.toString())
                    }
                    .addOnFailureListener() {

                    }
        }
    }


    private  fun saveDataToFirebaseDatabase (profileImageURL : String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User (uid, username_edittext_registration.text.toString(),profileImageURL )

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Register"," Saved to database")
                }
                .addOnFailureListener() {
                    Log.d("Register"," Saved to database")
                }

    }


    var selectedPhotoUrl : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUrl = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUrl)
            select_photo_imageview_register.setImageBitmap(bitmap)
        }
    }

}

class User (val uid : String, val username: String, val profileImageUri : String)