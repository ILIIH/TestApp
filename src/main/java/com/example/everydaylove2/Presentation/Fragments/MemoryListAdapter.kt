package com.example.everydaylove2.Presentation.Helpers

import android.animation.AnimatorSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.everydaylove2.R
import com.example.everydaylove2.databinding.MemoryCardBinding
import com.example.everydaylove2.domain.Memory
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoryListAdapter : ListAdapter<Memory, MemoryListAdapter.RepoViewHolder>(DiffCallback()) {

    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryListAdapter.RepoViewHolder {
        return RepoViewHolder.from(parent) as RepoViewHolder
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, front_anim, back_anim)
    }

    // ////////////////////////////////////////////////////////////////////////////////////

    fun addListAndSubmitList(list: List<Memory>, front: AnimatorSet, back: AnimatorSet) {
        adapterScope.launch {

            front_anim = front
            back_anim = back

            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    // ////////////////////////////////////////////////////////////////

    class RepoViewHolder private constructor(val binding: MemoryCardBinding) : RecyclerView.ViewHolder(binding.root) {

        lateinit var front_anim: AnimatorSet
        lateinit var back_anim: AnimatorSet

        fun bind(item: Memory, front: AnimatorSet, back: AnimatorSet) {

            front_anim = front
            back_anim = back

            binding.DescriptionText.text = item.Description
            binding.Year.text = item.year
            binding.date.text = item.date

            bindImage(item.Image)

            SetRotateAnimarion()

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MemoryCardBinding.inflate(layoutInflater, parent, false)
                return RepoViewHolder(binding)
            }
        }

        fun bindImage(url: String) {
            val storage = FirebaseStorage.getInstance() // Create a reference to a file from a Google Cloud Storage URI
            val gsReference = storage.getReferenceFromUrl("gs://every-day-love-2.appspot.com/images/$url.jpg")

            GlideApp.with(binding.Photo.context)
                .load(gsReference).apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .override(600, 500).centerCrop()
                .into(binding.Photo)
        }

        fun SetRotateAnimarion() {
            val frontCard = binding.CardFront; val backCard = binding.CardBack; frontCard.setOnClickListener {
                val aplha = frontCard.alpha
                when (aplha) {
                    0.0F -> {
                        front_anim.setTarget(backCard)
                        back_anim.setTarget(frontCard)
                        front_anim.start()
                        back_anim.start()
                    }
                    1.0F -> {
                        front_anim.setTarget(frontCard)
                        back_anim.setTarget(backCard)
                        front_anim.start()
                        back_anim.start()
                    }
                }
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Memory>() {
    override fun areItemsTheSame(oldItem: Memory, newItem: Memory): Boolean {
        return oldItem.Description.equals(newItem.Description)
    }
    override fun areContentsTheSame(oldItem: Memory, newItem: Memory): Boolean {
        return oldItem == newItem
    }
}
