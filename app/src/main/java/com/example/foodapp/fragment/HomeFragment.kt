package com.example.foodapp.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.FoodAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.viewmodel.HomeFragmentVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var allFoodAdapter: FoodAdapter
    private lateinit var viewModel: HomeFragmentVM
    private lateinit var auth: FirebaseAuth

    var foodMake : Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.fragmentHome = this

        auth = Firebase.auth
        binding.userName = auth.currentUser?.displayName

        binding.rvFood.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)


        binding.searchViewFood.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return true
            }
        })

        val bundle : HomeFragmentArgs by navArgs()

        binding.cartListState = bundle.pay




        // All Food Adappter
        viewModel.foodList.observe(viewLifecycleOwner,{
            allFoodAdapter = FoodAdapter(requireContext(),it,viewModel)
            binding.foodAdapter = allFoodAdapter
        })

        return  binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tempViewModel:HomeFragmentVM by viewModels()
        viewModel = tempViewModel
        viewModel.updateCartList()
    }

    fun toCart() {
        Navigation.findNavController(binding.root).navigate(R.id.toCart)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCartList()
    }

    fun signOut() {
        Firebase.auth.signOut()
        Navigation.findNavController(binding.root).navigate(R.id.toHomePage)

    }




}


