package uz.texnopos.instagram.ui.profile.edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.instagram.MainActivity
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.ResourceState
import uz.texnopos.instagram.data.model.User
import uz.texnopos.instagram.databinding.FragmentProfileEditBinding

class EditProfileFragment: Fragment(R.layout.fragment_profile_edit) {

    private lateinit var binding: FragmentProfileEditBinding
    private val viewModel: EditProfileViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var cUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCurrentUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileEditBinding.bind(view)
        navController = findNavController()

        setUpObServers()

        binding.apply {
            cancelBtn.setOnClickListener {
                navController.popBackStack()
            }
            doneBtn.setOnClickListener {
                cUser.biography = etBiography.text.toString()
                cUser.name = etName.text.toString()

                viewModel.editProfile(cUser)
            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.popBackStack(R.id.action_editProfileFragment_to_mainFragment, true)
                }
            }

        (requireActivity() as MainActivity).onBackPressedDispatcher.addCallback(requireActivity(), callback)

    }

    private fun setUpObServers(){

        viewModel.profileEdit.observe(viewLifecycleOwner, {
            when(it.status){
                ResourceState.LOADING -> {
                    setLoading(true)
                }
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    Toast.makeText(requireContext(), "Your profile successfully updated !", Toast.LENGTH_SHORT).show()
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.user.observe(viewLifecycleOwner, {
            when(it.status){
                ResourceState.LOADING -> {
                    setLoading(true)
                }
                ResourceState.SUCCESS -> {
                    binding.apply {

                        setLoading(false)

                        cUser = it.data!!

                        Glide
                            .with(this@EditProfileFragment)
                            .load(cUser.image)
                            .into(userImage)

                        etName.setText(cUser.name)
                        etBiography.setText(cUser.biography)

                    }
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setLoading(isLoading: Boolean){
        binding.apply {
            loading.isVisible = isLoading
            cancelBtn.isEnabled = !isLoading
            etName.isEnabled = !isLoading
            etBiography.isEnabled = !isLoading
        }
    }

}