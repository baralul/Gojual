package com.tam.gojual.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Data class ini merepresentasikan satu postingan (iklan) barang atau jasa.
 */
data class Post(
    // ID dokumen dari Firestore. Kita gunakan 'var' agar bisa diisi nanti.
    var id: String = "",

    // Data yang diisi oleh pengguna
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val type: String = "",      // "Barang" atau "Jasa"
    val category: String = "",
    val location: String = "",  // Kota/Kabupaten
    val contactWhatsapp: String = "",
    val contactPhone: String = "",
    val contactEmail: String = "",

    // Data Penjual (diambil saat postingan dibuat)
    val sellerUid: String = "",
    val sellerName: String = "",

    // Timestamp yang diisi otomatis oleh server Firebase.
    // Sangat berguna untuk mengurutkan postingan berdasarkan waktu.
    @ServerTimestamp
    val createdAt: Date? = null
)