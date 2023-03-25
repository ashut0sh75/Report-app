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
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class StorageActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityStorageBinding
    private lateinit var database : DatabaseReference
    private lateinit var  storage : StorageReference
    var item = arrayOf("Reason1", "Reason2", "Reason3", "Reason4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this@StorageActivity, android.R.layout.simple_dropdown_item_1line, item)
        val textInputLayout = findViewById<TextInputLayout>(R.id.droplist)
        val autoCompleteTextView = textInputLayout.findViewById<AutoCompleteTextView>(R.id.auto_complete_text_view)

        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 0

        autoCompleteTextView.setOnTouchListener { _, _ ->
            autoCompleteTextView.showDropDown()
            false
        }

        binding.ReportBtn.setOnClickListener{
            val simple =autoCompleteTextView.text.toString()
            Log.d(TAG, "Selected item: $simple")
            uploadText(simple)
        }
    }

    private fun uploadText(uploadtext: String) {
        if (uploadtext.isEmpty() || uploadtext !in item ) {
            Toast.makeText(this,"Please select a reason",Toast.LENGTH_SHORT).show()
            return
        }

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uploadtext).setValue(uploadtext).addOnSuccessListener {
            binding.autoCompleteTextView.text.clear()
            Toast.makeText(this,"Successfully Saved Text",Toast.LENGTH_SHORT).show()
            var intent = Intent(this@StorageActivity, feedback::class.java)
            uploadImage(uploadtext)
            startActivity(intent)


        }.addOnFailureListener{
            Toast.makeText(this,"Failed to upload text",Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(imageName: String){
        storage = FirebaseStorage.getInstance().getReference("Users")
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.flower2)
        val imageRef = storage.child("images/${imageName}")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val data = baos.toByteArray()

        imageRef.putBytes(data)
            .addOnSuccessListener {
                Toast.makeText(this,"Successfully Saved image",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to upload image",Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val TAG = "StorageActivity"
    }
}
