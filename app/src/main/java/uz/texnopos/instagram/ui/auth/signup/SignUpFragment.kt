package uz.texnopos.instagram.ui.auth.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.texnopos.instagram.R
import uz.texnopos.instagram.databinding.FragmentSignUpBinding

class SignUpFragment: Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    // todo usi jerge kelip toqtadiq
                }
            }
        }
    }
}