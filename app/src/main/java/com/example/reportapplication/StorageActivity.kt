package com.example.reportapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reportapplication.databinding.ActivityStorageBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class StorageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStorageBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private val item = arrayOf("Reason1", "Reason2", "Reason3", "Reason4")
    private var selectedImageResourceId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance().getReference("Users")
        setContentView(binding.root)

        selectedImageResourceId = intent.getIntExtra("image_resource_id", 0)
        binding.selectedImageView.setImageResource(selectedImageResourceId)

        // Set up the dropdown menu for the reasons
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, item)
        val textInputLayout = findViewById<TextInputLayout>(R.id.droplist)
        val autoCompleteTextView =
            textInputLayout.findViewById<AutoCompleteTextView>(R.id.auto_complete_text_view)

        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnTouchListener { _, _ ->
            autoCompleteTextView.showDropDown()
            false
        }

        binding.ReportBtn.setOnClickListener {
            val reason = autoCompleteTextView.text.toString()
            Log.d(TAG, "Selected reason: $reason")
            uploadFeedback(reason)
        }
    }

    private fun uploadFeedback(reason: String) {

        if (reason !in item) {
            Toast.makeText(this, "Please select a valid reason", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a unique key for the feedback item
        val feedbackKey = database.child(reason).push().key

        // Create a new child under the selected reason with the unique key and upload the text
        val feedbackRef = database.child(reason).child(feedbackKey!!)

        feedbackRef.child("feedback_/$reason").setValue(reason)
            .addOnSuccessListener {
                binding.autoCompleteTextView.text.clear()
                Toast.makeText(this, "Successfully saved text", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, feedback::class.java)
                uploadImage(reason, feedbackKey)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload text", Toast.LENGTH_SHORT).show()
            }
    }


    private fun uploadImage(reason: String, feedbackKey: String) {
        storage = FirebaseStorage.getInstance().getReference("Users")
        val imageRef = storage.child("images/$reason")

        val baos = ByteArrayOutputStream()

        BitmapFactory.decodeResource(resources, selectedImageResourceId)
            ?.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val data = baos.toByteArray()

        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()

        imageRef.putBytes(data, metadata)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully saved image", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val TAG = "StorageActivity"
    }
}
