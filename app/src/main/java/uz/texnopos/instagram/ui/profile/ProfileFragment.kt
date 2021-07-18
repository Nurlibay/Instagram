package uz.texnopos.instagram.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.ResourceState
import uz.texnopos.instagram.databinding.FragmentProfileBinding

class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        setUpObservers()
        viewModel.getProfileData()
    }

    private fun setUpObservers(){
        viewModel.profile.observe(viewLifecycleOwner, Observer {
            when(it.status){
                ResourceState.LOADING -> {
                    setLoading(true)
                }
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    binding.apply {
                        val user = it.data!!
                        tvUsername.text = user.email
                        tvBiography.text = user.biography
                        tvFollowersCount.text = user.followersCount.toString()
                        tvFollowingCount.text = user.followingsCount.toString()
                        tvPostCount.text = user.postCount.toString()

                        Glide
                            .with(this@ProfileFragment)
                            .load(user.image)
                            .centerCrop()
                            .into(avatar)
                    }
                }

                ResourceState.ERROR -> {
                    setLoading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setLoading(isLoading: Boolean){
        binding.apply {
            loading.isVisible = isLoading
            btnEdit.isEnabled = !isLoading
            rvPosts.isEnabled = !isLoading
            tvUsername.isEnabled = !isLoading
        }
    }
}