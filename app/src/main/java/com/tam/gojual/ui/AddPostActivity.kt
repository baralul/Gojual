package com.tam.gojual.ui // Sesuaikan dengan package Anda

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tam.gojual.R
import com.tam.gojual.data.model.Post
import com.tam.gojual.databinding.ActivityAddPostBinding
import com.tam.gojual.viewmodel.PostResult
import com.tam.gojual.viewmodel.PostViewModel

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdowns()
        setupListeners()
        observeViewModel()
    }

    // Fungsi untuk menyiapkan menu dropdown
    private fun setupDropdowns() {
        val postTypes = resources.getStringArray(R.array.post_types)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, postTypes)
        binding.actType.setAdapter(typeAdapter)

        // Mengubah pilihan kategori berdasarkan tipe iklan yang dipilih
        binding.actType.setOnItemClickListener { parent, _, position, _ ->
            val selectedType = parent.getItemAtPosition(position).toString()
            updateCategoryDropdown(selectedType)
            binding.tilCategory.visibility = View.VISIBLE
        }
    }

    // Fungsi untuk mengupdate isi dropdown kategori
    private fun updateCategoryDropdown(type: String) {
        val categories = if (type == "Barang") {
            resources.getStringArray(R.array.item_categories)
        } else {
            resources.getStringArray(R.array.service_categories)
        }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.actCategory.setAdapter(categoryAdapter)
        binding.actCategory.setText("", false) // Mengosongkan pilihan sebelumnya
    }

    // Fungsi untuk mengamati hasil dari ViewModel (Loading, Success, Error)
    private fun observeViewModel() {
        viewModel.postResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            when(result) {
                is PostResult.Success -> {
                    Toast.makeText(this, "Iklan berhasil diterbitkan!", Toast.LENGTH_SHORT).show()
                    finish() // Tutup halaman form setelah berhasil
                }
                is PostResult.Error -> {
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
                is PostResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    // Fungsi untuk menangani klik tombol
    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            submitPost()
        }
    }

    // Fungsi utama untuk validasi dan mengirim data
    private fun submitPost() {
        // Mengambil semua data dari form
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()
        val type = binding.actType.text.toString()
        val category = binding.actCategory.text.toString()
        val location = binding.etLocation.text.toString().trim()
        val whatsapp = binding.etWhatsapp.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val emailContact = binding.etEmailContact.text.toString().trim()

        // --- Validasi Input ---
        if (title.isEmpty()) {
            binding.tilTitle.error = "Judul tidak boleh kosong"
            return
        } else {
            binding.tilTitle.error = null
        }

        if (description.isEmpty()) {
            binding.tilDescription.error = "Deskripsi tidak boleh kosong"
            return
        } else {
            binding.tilDescription.error = null
        }

        if (price.isEmpty()) {
            binding.tilPrice.error = "Harga tidak boleh kosong"
            return
        } else {
            binding.tilPrice.error = null
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Pilih tipe iklan", Toast.LENGTH_SHORT).show()
            return
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Pilih kategori", Toast.LENGTH_SHORT).show()
            return
        }

        if (location.isEmpty()) {
            binding.tilLocation.error = "Lokasi tidak boleh kosong"
            return
        } else {
            binding.tilLocation.error = null
        }

        // Validasi kontak: pastikan minimal salah satu terisi
        if (whatsapp.isEmpty() && phone.isEmpty() && emailContact.isEmpty()){
            Toast.makeText(this, "Harap isi minimal satu informasi kontak", Toast.LENGTH_LONG).show()
            return
        }
        // --- Akhir Validasi ---

        // Membuat objek Post dari data yang sudah valid
        val post = Post(
            title = title,
            description = description,
            price = price,
            type = type,
            category = category,
            location = location,
            contactWhatsapp = whatsapp,
            contactPhone = phone,
            contactEmail = emailContact
        )

        // Mengirim objek Post ke ViewModel untuk disimpan
        viewModel.addPost(post)
    }
}