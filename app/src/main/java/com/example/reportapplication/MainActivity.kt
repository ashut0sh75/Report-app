package com.example.reportapplication

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.reportapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flowerName = arrayOf("Rose", "Lotus", "Lily", "Jasmine", "Tulip")
        val flowerImages = intArrayOf(R.drawable.flower2, R.drawable.flower3, R.drawable.flower4, R.drawable.flower5, R.drawable.flower6)

        val gridAdapter = GridAdapter(this, flowerName, flowerImages)
        binding.gridView.adapter = gridAdapter

        binding.gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            val intent = Intent(this@MainActivity, StorageActivity::class.java)
            startActivity(intent)
        }




        }

    }

