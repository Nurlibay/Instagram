package uz.texnopos.instagram.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.ResourceState
import uz.texnopos.instagram.databinding.FragmentProfileBinding
import uz.texnopos.instagram.ui.main.MainFragment

class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var binding: FragmentProfileBinding
//    private lateinit var postBinding: ItemPostBinding
    private lateinit var parentNavController: NavController
    private var adapter = ProfilePostAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
//        postBinding = ItemPostBinding.bind(LayoutInflater.from(requireActivity())
//            .inflate(R.layout.item_post, null, false))
        binding.rvPosts.adapter = adapter
        setUpObservers()
        viewModel.getProfileData()
        viewModel.getCurrentUserPosts()

        parentNavController = (parentFragment?.parentFragment as MainFragment).findNavController()

        binding.btnEdit.setOnClickListener {
            parentNavController.navigate(R.id.action_mainFragment_to_editProfileFragment)
        }
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
                        tvName.text = user.name
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

        viewModel.posts.observe(viewLifecycleOwner){
            when(it.status){
                ResourceState.LOADING -> {
                    setLoading(true)
                }
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    adapter.models = it.data!!
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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