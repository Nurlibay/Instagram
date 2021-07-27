package uz.texnopos.instagram.ui.profile.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import java.io.ByteArrayOutputStream

class EditProfileFragment: Fragment(R.layout.fragment_profile_edit) {

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
    }

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

                userImage.isDrawingCacheEnabled = true
                userImage.buildDrawingCache()
                val bitmap = (userImage.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                viewModel.sendNewUserImage(data)

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


        binding.tvEditImage.setOnClickListener {
            pickImageFromGallery()
        }
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

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.userImage.setImageURI(data?.data)
        }
    }

}