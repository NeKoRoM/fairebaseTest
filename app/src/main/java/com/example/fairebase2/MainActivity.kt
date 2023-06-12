package com.example.fairebase2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fairebase2.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, Wdddorld!")
        binding.button.setOnClickListener{
            Firebase.auth.signOut()
            finish()

        }
    }

    fun setUpActionBar(){
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator()

    }

}