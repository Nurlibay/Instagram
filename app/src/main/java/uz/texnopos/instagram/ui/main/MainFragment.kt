package uz.texnopos.instagram.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.koin.android.ext.android.inject
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.Settings
import uz.texnopos.instagram.databinding.FragmentMainBinding

class MainFragment: Fragment(R.layout.fragment_main) {

    private val settings: Settings by inject()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings.signedIn = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainer)
        binding.bnv.setupWithNavController(navController)
    }

}