package uz.texnopos.instagram.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.ResourceState
import uz.texnopos.instagram.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private val adapter = PostAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.rvPost.adapter = adapter

        adapter.setOnDoubleClickListener { post ->
            viewModel.onDoubleClicked(post)
        }
        setUpObservers()
        viewModel.getCurrentUserPosts()
    }

    private fun setUpObservers() {
        viewModel.posts.observe(viewLifecycleOwner){
            when(it.status){
                ResourceState.LOADING -> { }
                ResourceState.SUCCESS -> {
                    adapter.models = it.data!!.toMutableList()
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.like.observe(viewLifecycleOwner) {
            when(it.status){
                ResourceState.LOADING -> {
                }
                ResourceState.SUCCESS -> {
                    adapter.updatePost(it.data!!)
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}