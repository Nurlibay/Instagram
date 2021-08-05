package uz.texnopos.instagram.ui.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.ResourceState
import uz.texnopos.instagram.databinding.FragmentFavoriteBinding

class FavoriteFragment: Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: LikedPostViewModel by viewModel()
    private val adapter: LikedPostAdapter = LikedPostAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        binding.rvLikedPosts.adapter = adapter

        adapter.setOnDoubleClickListener { post ->
            viewModel.onDoubleClicked(post)
        }
        setUpObservers()
        viewModel.getUserLikedPosts()
    }

    private fun setUpObservers() {
        viewModel.likedPost.observe(viewLifecycleOwner){
            when(it.status){
                ResourceState.LOADING -> {
                    binding.loading.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    binding.loading.isVisible = false
                    adapter.models = it.data!!.toMutableList()
                }
                ResourceState.ERROR -> {
                    binding.loading.isVisible = false
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}