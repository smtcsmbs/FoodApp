package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.foodapp.R
import com.example.foodapp.adapter.CartAdapter
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.viewmodel.CartFragmentVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel : CartFragmentVM
    private lateinit var cartListAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false)
        binding.fragmentCart = this
        binding.totalAmount = "0"


        viewModel.cartFromList.observe(viewLifecycleOwner,{
            cartListAdapter = CartAdapter(requireContext(),it, viewModel)
            binding.adapter = cartListAdapter
            binding.piece = it.count().toString()
            viewModel.amount()
            binding.totalAmount = viewModel.total.toString()
            if (it.count() == 0) {
                binding.totalAmount = "0"
            }



        })




        return binding.root
    }


    fun toHome() {
        auth = Firebase.auth
        val email = auth.currentUser?.email
        val parts = email?.split("@")
        val userName = parts!![0]
        val pass: NavDirections
      if(binding.bttnPay.text.toString() == "Pay")  {
           pass = CartFragmentDirections.cartToHome(true)
          viewModel.cartFromList.observe(viewLifecycleOwner,{
              for(food in it) {
                  viewModel.delete(food.cart_food_id,userName)
              }
          })
      }
        else {
           pass = CartFragmentDirections.cartToHome(false)
       }

        Navigation.findNavController(binding.root).navigate(pass)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tempViewModel: CartFragmentVM by viewModels()
        viewModel = tempViewModel
    }




}