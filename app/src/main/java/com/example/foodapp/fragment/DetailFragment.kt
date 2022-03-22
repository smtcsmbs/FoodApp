package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentDetailBinding
import com.example.foodapp.entity.Food
import com.example.foodapp.viewmodel.DetailFragmentVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailFragmentVM
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail, container, false)
        binding.fragmentDetail = this

        val bundle : DetailFragmentArgs by navArgs()
        val getFood = bundle.food
        binding.foodObject = getFood

        getFoodImage(getFood.food_image_name)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tempViewModel:DetailFragmentVM by viewModels()
        viewModel = tempViewModel

    }


    fun getFoodImage(imageName:String) {
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/$imageName"
        Picasso.get().load(url).into(binding.imageViewFood)
    }

    fun buttonPlus() {
        val a =  binding.textViewPrice.text.toString().toInt() + 1
        binding.textViewPrice.text = a.toString()

    }


    fun buttonMinus() {
        if(binding.textViewPrice.text.toString() != "1") {
            val a =  binding.textViewPrice.text.toString().toInt() - 1
            binding.textViewPrice.text = a.toString()

        }

    }

    fun buttonToCart(food:Food) {
        auth = Firebase.auth
        val email = auth.currentUser?.email
        val parts = email?.split("@")
        val userName = parts!![0]
        val count = binding.textViewPrice.text.toString().toInt()
        viewModel.addFoodToCart(food.food_name,food.food_image_name,food.food_price,count,userName)

    }

}