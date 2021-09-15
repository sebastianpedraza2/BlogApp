package com.example.blogandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.blogandroidapp.core.hide
import com.example.blogandroidapp.core.show
import com.example.blogandroidapp.databinding.ActivityMainBinding
import com.example.blogandroidapp.di.dataSourceModule
import com.example.blogandroidapp.di.repositoryModule
import com.example.blogandroidapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Starting DI KOIN
         */
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(listOf(viewModelModule, repositoryModule, dataSourceModule))
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**
         *
         * El bottom navigation debe conocer el main graph para saber a que pantalla ir con ese id en cada item
         * Casteo el id del fragment container en el xml de la actividad como navhostfragment y saco el controlador de navegacion
         *
         */
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        observerDestinationChange()

    }

    private fun observerDestinationChange() {
        /**
         * Si estamos en el login o register entonces que no muestre el bottom navigation, usando extension functions
         */
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> {
                    binding.bottomNavigationView.hide()
                }
                R.id.registerFragment -> {
                    binding.bottomNavigationView.hide()

                }
                R.id.setupProfileFragment -> {
                    binding.bottomNavigationView.hide()

                }
                else -> {
                    binding.bottomNavigationView.show()

                }
            }
        }
    }
}