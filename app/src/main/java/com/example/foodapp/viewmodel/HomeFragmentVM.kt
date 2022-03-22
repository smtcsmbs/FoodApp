package com.example.foodapp.viewmodel

import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.entity.CartFood
import com.example.foodapp.entity.Food
import com.example.foodapp.repo.FoodRepository

class HomeFragmentVM :ViewModel() {
    var foodList = MutableLiveData<List<Food>>()
    val foodRepo = FoodRepository()
    var cartFromList = MutableLiveData<List<CartFood>>()


    init {
        updateFoods()
        updateCartList()
     foodList =  foodRepo.callFoods()
        cartFromList = foodRepo.callCartList()
    }

    fun updateFoods() {
        foodRepo.allFood()
    }

    fun updateCartList() {
        foodRepo.getAllFoodFromCart()
    }

    fun search(query:String) {
        if (query == "") {
            updateFoods()
        }
        else {
            foodRepo.searchFood(query)
        }
    }

    fun addFoodToCart(food_name:String, food_image_name :String, food_price:Int,  cart_food_piece:Int, userName:String) {
        foodRepo.addFoodtoCart(food_name, food_image_name, food_price,  cart_food_piece, userName)

    }







}