package com.example.reportapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reportapplication.databinding.ActivityFeedbackBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class feedback : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener {
            val text = binding.feedback.text.toString()
            uploadfeedback(text)
        }

        binding.noBtn.setOnClickListener {
            Toast.makeText(this, "Thank You for Reporting", Toast.LENGTH_SHORT).show()
            var intent = Intent(this@feedback, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadfeedback(text: String) {
        if (text.isEmpty()) {
            Toast.makeText(this, "Please Type Feedback", Toast.LENGTH_SHORT).show()
            return
        }

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child("Feedback").setValue(text).addOnSuccessListener {
            binding.feedback.text.clear()
            Toast.makeText(this, "Successfully Saved Feedback", Toast.LENGTH_SHORT).show()
            var intent = Intent(this@feedback, MainActivity::class.java)
            startActivity(intent)

        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload Feedback", Toast.LENGTH_SHORT).show()
        }
    }
}

