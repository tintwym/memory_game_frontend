package com.team8.memory_game_frontend.ui.register

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.team8.memory_game_frontend.databinding.ActivityRegisterBinding
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.team8.memory_game_frontend.R
import com.team8.memory_game_frontend.api.RetrofitClient
import com.team8.memory_game_frontend.data.model.request.AuthRequest
import com.team8.memory_game_frontend.ui.fetch.FetchActivity
import com.team8.memory_game_frontend.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var isPasswordVisible: Boolean = false

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            this,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            handleRegistration(username, password)
        }

        binding.backToLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun handleRegistration(username: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = AuthRequest(username, password)
                val response = RetrofitClient.api.register(request)

                withContext(Dispatchers.Main) {
                    sharedPreferences.edit()
                        .putString("userId", response.userId)
                        .putString("username", response.username)
                        .putBoolean("isPaidUser", response.isPaidUser)
                        .apply()

                    Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, FetchActivity::class.java))
                    finishAffinity()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity,
                        "Registration failed: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        val inputType = if (isPasswordVisible) {
            binding.passwordToggle.setImageResource(R.drawable.ic_visibility)
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.passwordToggle.setImageResource(R.drawable.ic_visibility_off)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        binding.passwordEditText.inputType = inputType
        binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
    }
}
