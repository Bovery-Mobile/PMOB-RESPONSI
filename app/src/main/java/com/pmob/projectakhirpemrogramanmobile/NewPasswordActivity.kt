package com.pmob.projectakhirpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pmob.projectakhirpemrogramanmobile.databinding.ActivityNewPasswordBinding

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            val password = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            when {
                password.isEmpty() || confirm.isEmpty() ->
                    Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()

                password.length < 8 ->
                    Toast.makeText(this, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show()

                password != confirm ->
                    Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()

                else -> {
                    // TODO: update password ke server
                    Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
