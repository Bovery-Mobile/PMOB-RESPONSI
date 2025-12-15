package com.pmob.projectakhirpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.pmob.projectakhirpemrogramanmobile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.isEnabled = false

        binding.cbTerms.setOnCheckedChangeListener { _, isChecked ->
            binding.btnRegister.isEnabled = isChecked
        }

        // ===== Button Register =====
        binding.btnRegister.setOnClickListener {

            // ✅ CEK CHECKBOX WAJIB
            if (!binding.cbTerms.isChecked) {
                Toast.makeText(
                    this,
                    "Anda harus menyetujui Syarat & Ketentuan",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            // 🔍 VALIDASI INPUT
            when {
                email.isEmpty() -> {
                    binding.etEmail.error = "Email wajib diisi"
                    return@setOnClickListener
                }

                password.isEmpty() -> {
                    binding.etPassword.error = "Password wajib diisi"
                    return@setOnClickListener
                }

                password.length < 6 -> {
                    binding.etPassword.error = "Password minimal 6 karakter"
                    return@setOnClickListener
                }

                confirmPassword.isEmpty() -> {
                    binding.etConfirmPassword.error = "Konfirmasi password wajib diisi"
                    return@setOnClickListener
                }

                password != confirmPassword -> {
                    binding.etConfirmPassword.error = "Password tidak sama"
                    return@setOnClickListener
                }
            }

            // 🔐 REGISTER FIREBASE
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    val user = auth.currentUser

                    user?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Email verifikasi telah dikirim. Silakan cek email Anda.",
                                Toast.LENGTH_LONG
                            ).show()

                            startActivity(Intent(this, VerifyEmailActivity::class.java))
                            finish()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Gagal mengirim email verifikasi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener { error ->
                    Toast.makeText(
                        this,
                        error.message ?: "Register gagal",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        // ===== Kembali ke Login =====
        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}

