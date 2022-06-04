package com.example.everydaylove2.Presentation.Fragments

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.everydaylove2.Framework.DAOMemories
import com.example.everydaylove2.Presentation.Helpers.MemoryListAdapter
import com.example.everydaylove2.R
import com.example.everydaylove2.databinding.FragmentThisDayAtHistoryBinding
import com.example.everydaylove2.di.MyApplication
import javax.inject.Inject

class MemoriesFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MyApplication).appComponent.dayathistoryInject(this)
    }

    @Inject
    lateinit var DatabaceMemory: DAOMemories

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val apater = MemoryListAdapter()

        val binding = FragmentThisDayAtHistoryBinding.inflate(layoutInflater)
        binding.refreshLayout.setOnRefreshListener {
            apater.notifyDataSetChanged()
            binding.refreshLayout.setRefreshing(false)
        }

        binding.MemoryList.adapter = apater

        DatabaceMemory.ReadAll()

        DatabaceMemory.loaded.observe(viewLifecycleOwner) { it ->
            if (it == true) {
                apater.addListAndSubmitList(
                    DatabaceMemory.memories,
                    AnimatorInflater.loadAnimator(
                        requireContext().applicationContext,
                        R.animator.front_animator
                    ) as AnimatorSet,
                    AnimatorInflater.loadAnimator(
                        requireContext().applicationContext,
                        R.animator.back_animation
                    ) as AnimatorSet
                )
                DatabaceMemory.loaded.postValue(false)
            }
        }

        return binding.root
    }
}
