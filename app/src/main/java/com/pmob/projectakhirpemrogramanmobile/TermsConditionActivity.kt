package com.pmob.projectakhirpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class TermsConditionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)

        val cbAgree = findViewById<CheckBox>(R.id.cbAgree)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        cbAgree.setOnCheckedChangeListener { _, isChecked ->
            btnContinue.isEnabled = isChecked
            btnContinue.alpha = if (isChecked) 1f else 0.5f
        }

        btnContinue.setOnClickListener {
            startActivity(Intent(this, SetupProfileActivity::class.java))
        }
    }
}
