package com.tam.gojual.ui // Sesuaikan dengan package Anda

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.tam.gojual.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Tampilkan data pengguna yang sedang login
        binding.tvUserName.text = currentUser?.displayName ?: "Nama Tidak Tersedia"
        binding.tvUserEmail.text = currentUser?.email ?: "Email Tidak Tersedia"

        // Setup tombol logout
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            // Pindah ke LoginActivity dan bersihkan semua activity sebelumnya
            // agar pengguna tidak bisa kembali ke halaman profil dengan tombol "Back"
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}