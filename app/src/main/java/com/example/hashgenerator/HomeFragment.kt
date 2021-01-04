package com.example.hashgenerator

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hashgenerator.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, hashAlgorithms)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // MENU IN ACTION BAR
        setHasOptionsMenu(true)


        binding.generateButton.setOnClickListener {
            onGenerateClick()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    private fun onGenerateClick() {

        if (binding.plainText.text.isEmpty()) {
            showSnackBar("Field Empty....")
        } else {
            lifecycleScope.launch {
                applyAnimation()
                navigateToSuccess(getHashData())
            }
        }
    }


    private fun getHashData(): String {
        val algorithm = binding.autoCompleteTextView.text.toString()
        val plainText = binding.plainText.text.toString()

        return homeViewModel.getHash(plainText, algorithm)

    }

    private suspend fun applyAnimation() {
        binding.generateButton.isClickable = false

        binding.titleTextView.animate().alpha(0f).duration = 400L
        binding.generateButton.animate().alpha(0f).duration = 400L
        binding.textInputLayout.animate().alpha(0f)
            .translationXBy(1200f).duration = 400L
        binding.plainText.animate().alpha(0f)
            .translationXBy(-1200f).duration = 400L

        delay(300)

        binding.successBackground.animate().alpha(1f).duration = 600L
        binding.successBackground.animate().rotationBy(720f).duration = 600L
        binding.successBackground.animate().scaleXBy(900f).duration = 800L
        binding.successBackground.animate().scaleYBy(900f).duration = 800L

        delay(500)

        binding.successImageView.animate().alpha(1f).duration = 100L

        delay(1500L)

    }

    private fun navigateToSuccess(hash: String) {
        val directions = HomeFragmentDirections.actionHomeFragmentToSucessFragment(hash)
        findNavController().navigate(directions)
    }

    private fun showSnackBar(mssg: String) {
        val snackBar = Snackbar.make(binding.rootLayout, mssg, Snackbar.LENGTH_SHORT)
        snackBar.setAction("OKAY") {

        }
        snackBar.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_menu) {
            binding.plainText.text.clear()
            showSnackBar("Cleared...")

            return true
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}