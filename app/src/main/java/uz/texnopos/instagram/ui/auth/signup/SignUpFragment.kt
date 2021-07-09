package uz.texnopos.instagram.ui.auth.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.ResourceState
import uz.texnopos.instagram.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignupViewModel by viewModel()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding = FragmentSignUpBinding.bind(view)
        binding.apply {
            btnSignUp.setOnClickListener {
                var success = true
                if(etEmail.text.isNullOrEmpty()){
                    etEmail.error = getString(R.string.fill_the_field)
                    success = false
                }
                if(etPassword.text.isNullOrEmpty()){
                    etPassword.error = getString(R.string.fill_the_field)
                    success = false
                }
                if(etConfirmPassword.text.isNullOrEmpty()){
                    etConfirmPassword.error = getString(R.string.fill_the_field)
                    success = false
                }

                if(!success) return@setOnClickListener
                if(etPassword.text.toString() != etConfirmPassword.text.toString()){
                    success = false
                    etConfirmPassword.error = getString(R.string.password_not_match)
                } else {
                    viewModel.signUp(etEmail.text.toString(), etPassword.text.toString())
                }
            }
        }
        setObservers()
    }

    private fun setObservers() {
        viewModel.signUpStatus.observe(viewLifecycleOwner, Observer {
            when(it.status){
                ResourceState.LOADING -> {
                    setLoading(true)
                }
                ResourceState.SUCCESS -> {
                    val action = SignUpFragmentDirections.actionSignUpFragmentToMainFragment()
                    navController.navigate(action)
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setLoading(isLoading: Boolean){
        binding.apply {
            loading.isVisible = isLoading
            etEmail.isEnabled = !isLoading
            etPassword.isEnabled = !isLoading
            etConfirmPassword.isEnabled =!isLoading
        }
    }
}