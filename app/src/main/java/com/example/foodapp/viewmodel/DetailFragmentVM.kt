package com.example.foodapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.entity.Food
import com.example.foodapp.repo.FoodRepository

class DetailFragmentVM : ViewModel() {
    val frepo = FoodRepository()

    init {

    }

    fun addFoodToCart(food_name:String, food_image_name :String, food_price:Int,  cart_food_piece:Int, userName:String) {

        frepo.addFoodtoCart(food_name, food_image_name, food_price,  cart_food_piece, userName)

    }





}