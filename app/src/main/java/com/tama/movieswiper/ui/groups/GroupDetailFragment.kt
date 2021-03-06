package com.tama.movieswiper.ui.groups

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tama.movieswiper.MainActivity
import com.tama.movieswiper.R
import com.tama.movieswiper.databinding.CreateGroupFragmentBinding
import com.tama.movieswiper.databinding.GroupDetailFragmentBinding

class GroupDetailFragment : Fragment() {

    private lateinit var groupDetailViewModel: GroupDetailViewModel
    private var _binding: GroupDetailFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        groupDetailViewModel = ViewModelProvider(this).get(GroupDetailViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = GroupDetailFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root

        var name = arguments?.getString("name")

        (activity as MainActivity).show_group_users(binding, arguments?.getString("name"))
        binding.groupNameText.text = arguments?.getString("name")

        binding.deleteButton.setOnClickListener {(activity as MainActivity).delete_group(name)}
        binding.leaveGroupButton.setOnClickListener {(activity as MainActivity).leave_group(name)}

        binding.showRecommendationButton.setOnClickListener { groupDetailViewModel.showRecommendation(name) }

        groupDetailViewModel.movieName.observe(viewLifecycleOwner, Observer { name ->
            binding.movieTitleGroup.text = name
        })

        groupDetailViewModel.movieDescription.observe(viewLifecycleOwner, Observer { description ->
            binding.movieDescriptionGroup.text = description
        })

        groupDetailViewModel.movieRating.observe(viewLifecycleOwner, Observer { rating ->
            binding.movieRatingGroup.text = rating
        })

        groupDetailViewModel.moviePoster.observe(viewLifecycleOwner, Observer { poster ->
            binding.moviePictureGroup.setImageBitmap(poster)
        })

        groupDetailViewModel.movieRuntime.observe(viewLifecycleOwner, Observer { runtime ->
            binding.movieRuntimeGroup.text = runtime
        })


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        groupDetailViewModel.setNavigationController(navController, binding)

        binding.backToGroupsButton.setOnClickListener { navController.navigate(R.id.navigation_groups) }
        binding.showMoreButtonGroup.setOnClickListener(){
            groupDetailViewModel.getMovieDetailedInfo()
        }
    }
}