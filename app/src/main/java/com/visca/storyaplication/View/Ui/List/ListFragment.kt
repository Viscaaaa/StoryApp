package com.visca.storyaplication.View.Ui.List

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.visca.storyaplication.Data.Response.ListStoryItem
import com.visca.storyaplication.R
import com.visca.storyaplication.Utils.isInternetAvailable
import com.visca.storyaplication.View.Ui.DetailStory.DetailStoryActivity
import com.visca.storyaplication.ViewModelFactory
import com.visca.storyaplication.databinding.FragmentListBinding
import java.net.SocketTimeoutException

class ListFragment : Fragment(){

    private lateinit var binding: FragmentListBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var adapter = StoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListStory.layoutManager = layoutManager
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        showRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            showRecyclerView()
        }
    }

    private fun showRecyclerView() {
        binding.swipeRefreshLayout.isRefreshing = false

        if (!isInternetAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No internet connection.", Toast.LENGTH_SHORT).show()
            return
        }
        try {
//            viewModel.listStory().observe(viewLifecycleOwner) { story ->
//                setListStory(story)
//            }
            viewModel.listStory.observe(viewLifecycleOwner) { story ->
                setListStory(story)

            }
        } catch (e: SocketTimeoutException) {
            Toast.makeText(requireContext(), getString(R.string.server_timeout), Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(),
                getString(R.string.an_error_occurred_please_try_again_later), Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun setListStory(listStory: PagingData<ListStoryItem>) {

        adapter.submitData(lifecycle, listStory)

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(requireContext(), DetailStoryActivity::class.java)
                intent.putExtra("idUser", data.id)
                intent.putExtra("name", data.name)
                intent.putExtra("desc", data.description)
                intent.putExtra("photo", data.photoUrl)
                startActivity(intent)
            }
        })
    }
}