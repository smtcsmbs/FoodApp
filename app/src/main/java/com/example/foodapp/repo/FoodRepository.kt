package com.example.foodapp.repo

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.entity.*
import com.example.foodapp.retrofit.ApiUtils
import com.example.foodapp.retrofit.FoodDaoInterface
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class FoodRepository {

    var foodList: MutableLiveData<List<Food>>
    var foodDao: FoodDaoInterface
    var cartFoodList : MutableLiveData<List<CartFood>>
    private lateinit var auth: FirebaseAuth

    init {
        foodDao = ApiUtils.getFoodDaoInterface()
        foodList = MutableLiveData()
        cartFoodList = MutableLiveData()
    }


    fun callFoods() : MutableLiveData<List<Food>> {
        return  foodList
    }

    fun callCartList() : MutableLiveData<List<CartFood>> {
        return  cartFoodList
    }







    fun allFood(){
        foodDao.allFood().enqueue(object :Callback<FoodResult>{
            override fun onResponse(call: Call<FoodResult>, response: Response<FoodResult>) {
                val list = response.body()!!.foods
                foodList.value = list
            }

            override fun onFailure(call: Call<FoodResult>, t: Throwable) {

            }

        })

    }

    fun searchFood(query:String){
        foodDao.allFood().enqueue(object :Callback<FoodResult>{
            override fun onResponse(call: Call<FoodResult>, response: Response<FoodResult>) {
                val liste = response.body()!!.foods

                val listFiltered : List<Food> = liste!!.filter { it.food_name.toLowerCase().contains(query.toLowerCase()) }

                foodList.value = listFiltered
            }

            override fun onFailure(call: Call<FoodResult>, t: Throwable) {

            }

        })

    }



    fun addFoodtoCart(food_name:String, food_image_name :String, yemek_price:Int,  cart_food_piece:Int, userName:String) {
        foodDao.insertFoodToCaert(food_name, food_image_name, yemek_price,  cart_food_piece, userName).enqueue(object  : Callback<CRUDResult>{
            override fun onResponse(call: Call<CRUDResult>?, response: Response<CRUDResult>?) {

            }

            override fun onFailure(call: Call<CRUDResult>?, t: Throwable?) {

            }

        })
    }
    fun getAllFoodFromCart() {
        auth = Firebase.auth
        val email = auth.currentUser?.email
        val parts = email?.split("@")
        val userName = parts!![0]
        foodDao.allFoodFromCart(userName).enqueue(object : Callback<CartResult>{
            override fun onResponse(call: Call<CartResult>, response: Response<CartResult>) {

                cartFoodList.value = response.body()!!.cartFoods
            }
            override fun onFailure(call: Call<CartResult>, t: Throwable) {
            }

        })
    }

    fun deleteFood(cart_food_id:Int,userName:String) {
        foodDao.deleteFoodToCart(cart_food_id,userName).enqueue(object : Callback<CRUDResult> {
            override fun onResponse(call: Call<CRUDResult>, response: Response<CRUDResult>) {

                if (cartFoodList.value != null && !cartFoodList.value!!.isEmpty()) {
                    cartFoodList!!.value!!.toMutableList().removeAt(cartFoodList.value!!.size-1)
                    cartFoodList.value = emptyList()
                }
                getAllFoodFromCart()
            }

            override fun onFailure(call: Call<CRUDResult>, t: Throwable) {

            }

        })
    }
}


