package uz.texnopos.instagram.ui.home

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.model.Post
import uz.texnopos.instagram.databinding.ItemPostBinding
import java.text.SimpleDateFormat

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    private var onDoubleClicked: (post: Post) -> Unit = {}
    fun setOnDoubleClickListener(onDoubleClicked: (post: Post) -> Unit) {
        this.onDoubleClicked = onDoubleClicked
    }

    private var onDisliked: (post: Post, userId: String) -> Unit = { _, _ ->  }
    fun setOnDislikeListener(onDisliked: (post: Post, userId: String) -> Unit) {
        this.onDisliked = onDisliked
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun populateModel(post: Post) {
            binding.tvDescription.text = post.description
            binding.tvLikesCount.text = post.likedUsers.size.toString()
            if(post.likedUsers.contains(currentUser.uid)){
                binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            Glide
                .with(binding.root.context)
                .load(post.imageUrl)
                .into(binding.postImage)

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val dateString = simpleDateFormat.format(post.createdDate)
            binding.tvCreatedDate.text = String.format("Date: %s", dateString)

            var doubleClick = false
            binding.postImage.setOnClickListener {
                if (doubleClick) {
                    showLikeAnimation()
                    onDoubleClicked.invoke(post)
                    doubleClick = false
                } else {
                    doubleClick = true
                    GlobalScope.launch {
                        delay(500)
                        doubleClick = false
                    }
                }
            }

            binding.favoriteIcon.setOnClickListener {
                if(post.likedUsers.contains(currentUser.uid)){
                    binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    onDisliked.invoke(post, currentUser.uid)
                } else {
                    binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                    onDoubleClicked.invoke(post)
                }
            }
        }

        private fun showLikeAnimation() {
            binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
            binding.likeAnimation.apply {
                isVisible = true
                setMinAndMaxFrame(10, 40)
                speed = 2f
                addAnimatorListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        isVisible = false
                        //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }
                })
                playAnimation()
            }
        }
    }

    var models: MutableList<Post> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemBinding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }

    fun updatePost(post: Post) {
        val curPost = models.find { it.id == post.id }
        val index = models.indexOf(curPost)
        models[index] = post
        notifyItemChanged(index)
    }
}