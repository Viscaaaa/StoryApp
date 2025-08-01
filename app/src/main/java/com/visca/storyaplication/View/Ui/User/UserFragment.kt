package com.visca.storyaplication.View.Ui.User

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.visca.storyaplication.Data.Response.ListStoryItem
import com.visca.storyaplication.Data.Response.Response
import com.visca.storyaplication.R
import com.visca.storyaplication.View.Ui.DetailStory.DetailStoryActivity
import com.visca.storyaplication.View.Ui.Login.LoginViewModel
import com.visca.storyaplication.ViewModelFactory
import com.visca.storyaplication.databinding.FragmentUserBinding


class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val userViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var adapter = UserAdapter()

    private var nameUser = ""

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(getString(R.string.logout_confirm))
            builder.setPositiveButton("Yes") { dialog , _ ->
                userViewModel.logout()
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog , _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMyListStory.layoutManager = layoutManager
        binding.rvMyListStory.adapter = adapter

        userViewModel.getSession().observe(requireActivity()) {
            nameUser = it.username
        }

        viewModel.getMyStory(nameUser).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {
                    binding.pbMyListStory.visibility = View.VISIBLE
                }
                is Response.Success -> {
                    binding.pbMyListStory.visibility = View.GONE

                    if (result.data.isNotEmpty()) {
                        binding.tvNoData.visibility = View.GONE
                        setListMyStory(result.data)
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
                is Response.Error -> {
                    binding.pbMyListStory.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    private fun setListMyStory(listStory: List<ListStoryItem>) {
        adapter.submitList(listStory)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(requireContext(), DetailStoryActivity::class.java)
                intent.putExtra("idUser", data.id)
                startActivity(intent)
            }
        })
    }
}