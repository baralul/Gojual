package com.tam.gojual.data.model

/**
 * Data class ini merepresentasikan seorang pengguna di dalam aplikasi.
 * Field diinisialisasi dengan nilai default ("") untuk menghindari error saat
 * data dari Firestore dikonversi kembali menjadi objek.
 */
data class User(
    val uid: String = "",       // ID unik dari Firebase Authentication
    val name: String = "",      // Nama pengguna
    val email: String = ""      // Email yang digunakan untuk mendaftar
)