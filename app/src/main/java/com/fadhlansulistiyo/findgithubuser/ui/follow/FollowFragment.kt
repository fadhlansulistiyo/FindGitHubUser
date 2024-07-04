package com.fadhlansulistiyo.findgithubuser.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhlansulistiyo.findgithubuser.data.remote.response.FollowResponseItem
import com.fadhlansulistiyo.findgithubuser.data.repo.ResultState
import com.fadhlansulistiyo.findgithubuser.databinding.FragmentFollowBinding
import com.fadhlansulistiyo.findgithubuser.ui.ViewModelFactory

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FollowViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_POSITION, 0)
        val username = activity?.intent?.getStringExtra(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager

        observeData()

        if (index == 1 && username != null) {
            viewModel.getFollowingList(username)
        } else if (username != null) {
            viewModel.getFollowersList(username)
        }
    }

    private fun observeData() {
        viewModel.followData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)

                    val data = result.data
                    if (data.isEmpty()) {
                        showEmptyState(true)
                    } else {
                        showEmptyState(false)
                        setFollowAdapter(data)
                    }
                }
                is ResultState.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setFollowAdapter(user: List<FollowResponseItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(user)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.apply {
            ivEmptyFollow.visibility = if (isEmpty) View.VISIBLE else View.GONE
            rvFollow.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}