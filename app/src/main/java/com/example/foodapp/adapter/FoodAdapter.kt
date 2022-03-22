package com.example.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.entity.Food
import com.example.foodapp.databinding.FoodCardDesignBinding
import com.example.foodapp.fragment.HomeFragmentDirections
import com.example.foodapp.viewmodel.HomeFragmentVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class FoodAdapter(var mContext:Context,var foodList: List<Food>, var viewModel: HomeFragmentVM)
    : RecyclerView.Adapter<FoodAdapter.CardDesignConservative>() {
    private lateinit var auth: FirebaseAuth

    inner class CardDesignConservative(foodCardDesignBinding : FoodCardDesignBinding)
        : RecyclerView.ViewHolder(foodCardDesignBinding.root) {
        var foodCardDesignBinding : FoodCardDesignBinding
         init {
             this.foodCardDesignBinding = foodCardDesignBinding
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignConservative {
        val layoutInflater = LayoutInflater.from(mContext)
        val design = FoodCardDesignBinding.inflate(layoutInflater,parent,false)
        return  CardDesignConservative(design)
    }

    override fun onBindViewHolder(holder: CardDesignConservative, position: Int) {
        val food = foodList.get(position)
        val cardDesign = holder.foodCardDesignBinding
        cardDesign.foodObject = food
        cardDesign.piece = "1"
        getFoodImage(food.food_image_name,cardDesign.foodImageView)
        cardDesign.foodCard.setOnClickListener {
            val pass = HomeFragmentDirections.toDetails(food)
            Navigation.findNavController(it).navigate(pass)
        }

        cardDesign.bttnPricePlus.setOnClickListener {
         val a =  cardDesign.textViewPrice.text.toString().toInt() + 1
            cardDesign.textViewPrice.text = a.toString()

        }

        cardDesign.buttonMinus.setOnClickListener {
            if(cardDesign.textViewPrice.text.toString() != "1") {
                val a =  cardDesign.textViewPrice.text.toString().toInt() - 1
                cardDesign.textViewPrice.text = a.toString()
            }


        }
        auth = Firebase.auth
        val email = auth.currentUser?.email
        val parts = email?.split("@")
        val userName = parts!![0]

        cardDesign.bttnAddCart.setOnClickListener {

            val piece = cardDesign.textViewPrice.text.toString().toInt()
            viewModel.addFoodToCart(food.food_name,food.food_image_name,food.food_price,piece,userName)
            viewModel.updateCartList()

        }

    }


    override fun getItemCount(): Int {
        return foodList.size
    }


    fun getFoodImage(imageName:String,imageView:ImageView) {
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/$imageName"
         Picasso.get().load(url).into(imageView)
    }
}