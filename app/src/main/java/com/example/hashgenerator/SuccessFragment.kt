package com.example.hashgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.hashgenerator.databinding.FragmentSucessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessFragment : Fragment() {

    private var _binding: FragmentSucessBinding? = null
    private val binding get() = _binding!!

    private val args: SuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSucessBinding.inflate(inflater, container, false)

        binding.hashTextView.text = args.hash

        binding.copyButton.setOnClickListener {
            onCopyClick()
        }

        return binding.root
    }

    private fun onCopyClick() {
        lifecycleScope.launch {
            copyToClipboard(args.hash)
            applyAnimations()
        }
    }

    private fun copyToClipboard(hash: String) {
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Encrypted Text", hash)

        clipboardManager.setPrimaryClip(clipData)
    }

    private suspend fun applyAnimations() {
        binding.include.messageBackground.animate().translationY(80f).duration = 200L
        binding.include.messageTextView.animate().translationY(80f).duration = 200L

        delay(2000L)

        binding.include.messageBackground.animate().translationY(-80f).duration = 500L
        binding.include.messageTextView.animate().translationY(-80f).duration = 500L
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}