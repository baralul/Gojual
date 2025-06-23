package com.tam.gojual.ui // Sesuaikan dengan package Anda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handler untuk menunda sedikit agar splash screen terlihat (opsional)
        android.os.Handler(mainLooper).postDelayed({
            // Periksa status login pengguna saat ini
            if (FirebaseAuth.getInstance().currentUser == null) {
                // Jika tidak ada pengguna yang login, arahkan ke LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                // Jika sudah ada pengguna yang login, arahkan ke MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            }
            // Tutup SplashActivity agar tidak bisa kembali ke sini dengan tombol "Back"
            finish()
        }, 1000) // Tunda selama 1 detik (1000 milidetik)
    }
}