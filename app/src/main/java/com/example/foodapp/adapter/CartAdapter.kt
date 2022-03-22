package com.example.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.CartCardDesignBinding
import com.example.foodapp.entity.CartFood
import com.example.foodapp.viewmodel.CartFragmentVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class CartAdapter(var mContext: Context, var foodCartList: List<CartFood>,
                  var viewModel: CartFragmentVM)
    : RecyclerView.Adapter<CartAdapter.CardDesignConservative>(){
    private lateinit var auth: FirebaseAuth

        inner class CardDesignConservative(myOrderFoodCartDesignBinding: CartCardDesignBinding)
            :RecyclerView.ViewHolder(myOrderFoodCartDesignBinding.root) {
                var myOrderFoodCartDesignBinding : CartCardDesignBinding
                init {
                    this.myOrderFoodCartDesignBinding = myOrderFoodCartDesignBinding
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignConservative {
        val layoutInflater = LayoutInflater.from(mContext)
        val design = CartCardDesignBinding.inflate(layoutInflater,parent,false)
        return  CardDesignConservative(design)
    }

    override fun onBindViewHolder(holder: CardDesignConservative, position: Int) {
        val food = foodCartList.get(position)
        val cardDesign = holder.myOrderFoodCartDesignBinding
        cardDesign.foodCartObject = food
        getFoodImage(food.food_image_name,cardDesign.orderImage)
        auth = Firebase.auth
        val email = auth.currentUser?.email
        val parts = email?.split("@")
        val userName = parts!![0]
        cardDesign.imageView.setOnClickListener {
            viewModel.delete(food.cart_food_id,userName)
            viewModel.loadCartFoodList()





        }



    }

    override fun getItemCount(): Int {
        return  foodCartList.size
    }


    fun getFoodImage(imageName:String,imageView: ImageView) {


        val url = "http://kasimadalan.pe.hu/yemekler/resimler/$imageName"
        Picasso.get().load(url).into(imageView)



    }




}