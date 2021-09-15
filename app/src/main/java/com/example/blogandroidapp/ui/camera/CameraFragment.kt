package com.example.blogandroidapp.ui.camera

import android.app.Activity
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
import com.example.blogandroidapp.databinding.FragmentCameraBinding
import com.example.blogandroidapp.presentation.camera.CameraViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var binding: FragmentCameraBinding

    private val REQUEST_IMAGE_CAPTURE = 1

    private val cameraViewModel: CameraViewModel by viewModel()

    private var imageBitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)
        startCamera()
        selectPhoto()
        validatePostData()
    }

    private fun selectPhoto() {
        binding.uploadImageImageView.setOnClickListener {
            startCamera()
        }
    }

    private fun validatePostData() {
        binding.uploadImageButton.setOnClickListener {
            /**
             * Si el usuario escogio una imagen
             */
            if (imageBitmap != null) {
                val description = binding.editTextDescription.text.toString().trim()
                if (description.isEmpty()) {
                    binding.editTextDescription.error = "Username can't be empty"
                    return@setOnClickListener
                    /**
                     *  Si el capo username es vacio entonces devolver el setOnClickListener y no seguir con la ejecucion, como un break
                     */
                }
                createPost(description)

            } else {
                Toast.makeText(
                    requireContext(),
                    "You have to select a photo!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createPost(description: String) {
        cameraViewModel.createPost(
            imageBitmap = imageBitmap!!,
            imageDescription = description
        ).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                    binding.uploadImageProgressBar.show()
                    binding.uploadImageButton.hide()

                }
                is Resource.Success -> {

                    binding.uploadImageProgressBar.hide()
                    binding.uploadImageButton.show()
                    /**
                     * Vulevo al home
                     */
                    findNavController().popBackStack()

                }
                is Resource.Failure -> {

                    binding.uploadImageProgressBar.hide()
                    binding.uploadImageButton.show()
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


    /**
     * Para inciar la camara hago un intent y lo empiezo con el intent y un codigo que defino
     */
    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

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

    /**
     * Apenas termine el intent llega aca y puedo recoger la data que me llega (la imagen que quede en la variable data. extras y en un mapa adentro con el key data).
     * Esa imagen se la seteo al imageview del xml
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.uploadImageImageView.setImageBitmap(imageBitmap)

            this.imageBitmap = imageBitmap


        }
    }
}
