package com.team8.memory_game_frontend.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.team8.memory_game_frontend.ui.fetch.FetchActivity
import com.team8.memory_game_frontend.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
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

        val userId = sharedPreferences.getString("userId", null)
        val intent = Intent(this, if (userId != null && sharedPreferences.getString("username", "Guest") != "Guest") FetchActivity::class.java else LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
