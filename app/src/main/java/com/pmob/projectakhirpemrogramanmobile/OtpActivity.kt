package com.pmob.projectakhirpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pmob.projectakhirpemrogramanmobile.databinding.ActivityOtpBinding

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnVerify.setOnClickListener {
            val otp = binding.otp1.text.toString() +
                    binding.otp2.text.toString() +
                    binding.otp3.text.toString() +
                    binding.otp4.text.toString()

            if (otp.length < 4) {
                Toast.makeText(this, "Kode OTP belum lengkap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: verifikasi OTP
            startActivity(Intent(this, NewPasswordActivity::class.java))
        }
    }
}
