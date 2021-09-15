package com.example.blogandroidapp.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogandroidapp.R
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.core.hide
import com.example.blogandroidapp.core.show
import com.example.blogandroidapp.databinding.FragmentSetupProfileBinding
import com.example.blogandroidapp.presentation.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding

    private val REQUEST_IMAGE_CAPTURE = 1

    private val authViewModel: AuthViewModel by viewModel()

    private var imageBitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)
        startCamera()
        setupImage()
    }

    private fun setupImage() {
        binding.setupProfileButton.setOnClickListener {
            imageBitmap?.let {
                val username = binding.editTextUsername.text.toString().trim()
                if (username.isEmpty()) {
                    binding.editTextUsername.error = "Username can't be empty"
                    return@setOnClickListener
                    /**
                     *  Si el capo username es vacio entonces devolver el setOnClickListener y no seguir con la ejecucion, como un break
                     */
                }

                val alertDialog =
                    AlertDialog.Builder(requireContext()).setTitle("Uploading photo...").create()
                /**
                 * Named parameters, it representa el bitmap con que se hizo el let
                 */
                authViewModel.updateProfileData(imageBitmap = it, username = username)
                    .observe(viewLifecycleOwner, Observer { result ->
                        when (result) {
                            is Resource.Loading -> {
                                alertDialog.show()
                                binding.setupProfileBar.show()
                                binding.setupProfileButton.hide()

                            }
                            is Resource.Success -> {
                                alertDialog.dismiss()

                                binding.setupProfileBar.hide()
                                binding.setupProfileButton.show()
                                findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)

                            }
                            is Resource.Failure -> {
                                alertDialog.dismiss()
                                binding.setupProfileBar.hide()
                                binding.setupProfileButton.show()
                                /**
                                 * Show a toast if failed
                                 */
                                Toast.makeText(
                                    requireContext(),
                                    "Error: ${result.exception.message}",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    })

            }
        }
    }

    /**
     * Para inciar la camara hago un intent y lo empiezo con el intent y un codigo que defino
     */
    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        binding.profileImageView.setOnClickListener {
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "No se encontro una app para abrir la camara",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    /**
     * Apenas termine el intent llega aca y puedo recoger la data que me llega (la imagen que quede en la variable data. extras y en un mapa adentro con el key data).
     * Esa imagen se la seteo al imageview del xml
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImageView.setImageBitmap(imageBitmap)
            this.imageBitmap = imageBitmap
        }
    }
}