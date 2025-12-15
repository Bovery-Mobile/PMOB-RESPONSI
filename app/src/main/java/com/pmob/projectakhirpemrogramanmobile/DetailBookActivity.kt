package com.pmob.projectakhirpemrogramanmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pmob.projectakhirpemrogramanmobile.databinding.ActivityDetailBookBinding

class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")

        binding.tvTitle.text = title
        binding.tvAuthor.text = author

        // Back
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
