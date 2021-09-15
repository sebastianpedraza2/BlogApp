package com.example.blogandroidapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.blogandroidapp.R
import com.example.blogandroidapp.core.Resource
import com.example.blogandroidapp.core.hide
import com.example.blogandroidapp.core.show
import com.example.blogandroidapp.data.remote.home.HomeScreenDataSource
import com.example.blogandroidapp.databinding.FragmentHomeScreenBinding
import com.example.blogandroidapp.domain.home.HomeScreenRepoImpl
import com.example.blogandroidapp.presentation.home.HomeScreenViewModel
import com.example.blogandroidapp.presentation.home.HomeScreenViewModelFactory
import com.example.blogandroidapp.ui.home.adapter.HomeScreenAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding

    /**
     * Inyectando las dependencias con Koin
     */
    private val viewModel: HomeScreenViewModel by viewModel()

    /**
     * Inyeccion manual con viewModelFactory
     */
//    private val viewModel by viewModels<HomeScreenViewModel> {
//        HomeScreenViewModelFactory(
//            HomeScreenRepoImpl(
//                HomeScreenDataSource()
//            )
//        )
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)
        /**
         * Tambien puedo pedir la instancia NO LAZY de esta manera
         */
//        val viewModel: HomeScreenViewModel = getViewModel()

        viewModel.fetchLatestPost().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("LoadingPOSTS", "Loading ")

                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    Log.d("LoadingPOSTS", "POST CARGADOS ")

                    /**
                     * Resource.Success.data en donde queda la informacion
                     */
                    binding.progressCircular.visibility = View.GONE

                    /**
                     * Show message if there are no posts
                     */
                    binding.noPostLayout.apply {
                        if (result.data.isEmpty()) this.show() else this.hide()
                    }

                }
                is Resource.Failure -> {
                    Log.d("LoadingPOSTS", "POST NO CARGADOS ")
                    binding.progressCircular.visibility = View.GONE

                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un error: ${result.exception}", Toast.LENGTH_SHORT
                    ).show()

                }
            }
        })
    }


}