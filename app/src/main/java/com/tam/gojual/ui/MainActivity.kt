// Pastikan nama package ini cocok dengan proyek Anda
package com.tam.gojual.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tam.gojual.R
import com.tam.gojual.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Variabel untuk ViewBinding agar mudah mengakses elemen UI dari layout XML
    private lateinit var binding: ActivityMainBinding
    // Variabel untuk NavController untuk mengelola navigasi antar fragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Setup ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Daftarkan Toolbar sebagai ActionBar resmi untuk Activity ini.
        // Baris ini penting agar Fragment bisa menambahkan menu ke dalamnya.
        setSupportActionBar(binding.toolbar)

        // 3. Setup NavController
        // Kita cari host navigasi dari layout XML dan dapatkan controller-nya
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 4. Panggil fungsi untuk setup listener navigasi bawah
        setupBottomNavListener()
    }

    /**
     * Fungsi ini menyiapkan listener khusus untuk BottomNavigationView.
     * Tujuannya agar kita bisa menangani klik pada item "Tambah" secara berbeda
     * (yaitu dengan membuka Activity baru), sementara item lain tetap menggunakan NavController.
     */
    private fun setupBottomNavListener() {
        // Atur listener untuk setiap item yang dipilih di navigasi bawah
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Jika item Home atau Profil yang diklik
                R.id.homeFragment, R.id.profileFragment -> {
                    // Gunakan NavController untuk pindah ke fragment yang sesuai
                    navController.navigate(item.itemId)
                    true // Return true menandakan item ini sekarang terpilih secara visual
                }
                // Jika item "Tambah" yang diklik
                R.id.addPostFragment -> {
                    // Buka AddPostActivity sebagai halaman baru
                    startActivity(Intent(this, AddPostActivity::class.java))
                    // Return false agar item "Tambah" tidak terlihat aktif.
                    // Ini membuat seleksi visual tetap berada di fragment sebelumnya (Home atau Profil).
                    false
                }
                else -> false
            }
        }

        // Listener ini penting untuk memastikan ikon di BottomNavigationView tetap
        // sinkron dan terpilih dengan benar saat pengguna menekan tombol "Back"
        // untuk berpindah antar fragment.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavView.menu.findItem(destination.id)?.isChecked = true
        }
    }
}