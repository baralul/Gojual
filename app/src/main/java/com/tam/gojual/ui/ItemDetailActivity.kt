package com.tam.gojual.ui // Sesuaikan package

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tam.gojual.data.model.Post
import com.tam.gojual.databinding.ActivityItemDetailBinding
import com.tam.gojual.viewmodel.ItemDetailViewModel

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding
    private val viewModel: ItemDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getStringExtra("POST_ID")

        if (postId == null) {
            Toast.makeText(this, "ID Postingan tidak valid.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.fetchPostDetails(postId)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.post.observe(this) { post ->
            binding.progressBarDetail.visibility = View.GONE
            if (post != null) {
                displayPostDetails(post)
            } else {
                Toast.makeText(this, "Gagal memuat detail postingan.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayPostDetails(post: Post) {
        binding.tvDetailTitle.text = post.title
        binding.tvDetailPrice.text = post.price
        binding.tvDetailDescription.text = post.description
        binding.tvDetailSellerName.text = "Penjual: ${post.sellerName}"
        binding.tvDetailLocation.text = post.location

        // Periksa apakah pengguna sudah login atau belum
        if (FirebaseAuth.getInstance().currentUser != null) {
            // Jika sudah login, tampilkan semua tombol kontak yang tersedia
            binding.layoutContactButtons.visibility = View.VISIBLE

            if (post.contactWhatsapp.isNotBlank()) {
                binding.btnWhatsapp.visibility = View.VISIBLE
                binding.btnWhatsapp.setOnClickListener {
                    openWhatsApp(post.contactWhatsapp)
                }
            } else {
                binding.btnWhatsapp.visibility = View.GONE
            }

            if (post.contactPhone.isNotBlank()) {
                binding.btnPhone.visibility = View.VISIBLE
                binding.btnPhone.setOnClickListener {
                    openPhoneDialer(post.contactPhone)
                }
            } else {
                binding.btnPhone.visibility = View.GONE
            }

            if (post.contactEmail.isNotBlank()) {
                binding.btnEmail.visibility = View.VISIBLE
                binding.btnEmail.setOnClickListener {
                    openEmailClient(post.contactEmail, post.title)
                }
            } else {
                binding.btnEmail.visibility = View.GONE
            }
        }
    }

    private fun openWhatsApp(number: String) {
        try {
            // Format nomor ke format internasional tanpa + atau 0 di depan
            val formattedNumber = number.replaceFirst("^0", "62")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$formattedNumber")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Aplikasi WhatsApp tidak terinstall.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openPhoneDialer(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun openEmailClient(email: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Tanya Mengenai Iklan: $subject")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Tidak ada aplikasi email yang terinstall.", Toast.LENGTH_SHORT).show()
        }
    }
}