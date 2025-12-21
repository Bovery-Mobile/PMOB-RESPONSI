package com.pmob.projectakhirpemrogramanmobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pmob.projectakhirpemrogramanmobile.databinding.ActivityBuyBinding
import java.text.NumberFormat
import java.util.Locale

class BuyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val title = intent.getStringExtra("BOOK_TITLE") ?: "Unknown Book"
        val price = intent.getDoubleExtra("BOOK_PRICE", 0.0)
        val cover = intent.getStringExtra("BOOK_COVER")

        // Set data ke UI
        binding.tvTitle.text = title
        binding.tvPrice.text = formatRupiah(price)

        Glide.with(this)
            .load(cover)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.ivCover)

        // Back
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Buy now (dummy)
        binding.btnBuyNow?.setOnClickListener {
            Toast.makeText(this, "Checkout berhasil (dummy)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatRupiah(value: Double): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(value)
    }
}
