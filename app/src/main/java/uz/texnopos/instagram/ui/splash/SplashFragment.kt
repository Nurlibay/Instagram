package uz.texnopos.instagram.ui.splash

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.Settings
import uz.texnopos.instagram.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding = FragmentSplashBinding.bind(view)
        requireActivity().actionBar?.hide()

        val settings = Settings(requireContext())

        binding.lottieView.setMaxProgress(0.6f)
        binding.lottieView.addAnimatorListener(object: Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if(settings.signedIn){
                    navController.navigate(R.id.action_splashFragment_to_mainFragment)
                } else {
                    navController.navigate(R.id.action_splashFragment_to_signInFragment)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

}