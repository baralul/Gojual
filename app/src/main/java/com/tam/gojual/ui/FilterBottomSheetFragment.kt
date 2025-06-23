package com.tam.gojual.ui // Sesuaikan package

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tam.gojual.databinding.DialogFilterBinding

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Logika untuk tombol "Terapkan" dan "Reset" akan kita tambahkan di sini nanti
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}