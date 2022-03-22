package com.example.foodapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.foodapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class LoginFragment : Fragment() {


    private lateinit var binding: com.example.foodapp.databinding.FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.fragmentLoginObject = this

        auth = Firebase.auth
        cardViewStatus()

        return binding.root
    }



    fun buttonSignUp() {
        if (binding.cardViewSingIn.visibility == View.VISIBLE) {


            if (binding.edtEmailAdres.text.toString() == "" ||
                binding.edTPassword.text.toString() == "") {
                alertMessage(R.string.alert_message_null)
            }

            else {
                auth.signInWithEmailAndPassword(
                    binding.edtEmailAdres.text.toString(),
                    binding.edTPassword.text.toString()
                )
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {

                            Navigation.findNavController(binding.root).navigate(R.id.toHomePage)

                        } else {
                            alertMessage(R.string.alert_message)
                        }
                    }
            }

        } else {

            if(binding.edtEmailSingUp.text.toString() == "" ||
                binding.edtPasswordSingUp.text.toString()== "" ||
                binding.edtName.text.toString() == "") {
                alertMessage(R.string.alert_message_null)

            }

            else {
                auth.createUserWithEmailAndPassword(
                    binding.edtEmailSingUp.text.toString(),
                    binding.edtPasswordSingUp.text.toString()
                )
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder().apply {
                                displayName = binding.edtName.text.toString()
                            }.build()
                            user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Navigation.findNavController(binding.root).navigate(R.id.toHomePage)

                                }
                            }


                        } else {

                            if ("${task.exception}" == "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.") {
                                alertMessage(R.string.alert_message_same_accomt)

                            }

                            if("${task.exception}" == "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]") {
                                alertMessage(R.string.alert_message_password_char)
                            }

                            if ("${task.exception}" == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") {
                                alertMessage(R.string.alert_message_email_badly_format)
                            }

                            Log.e("Hata","${task.exception}")


                        }
                    }
            }

        }

    }





    // Daha önceden giriş yapılıp yapılmadığı kontrol edilmektedir.
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null ) {
            Navigation.findNavController(binding.root).navigate(R.id.toHomePage)
        }

    }




    // CardViewlerin görünürlük durumunu ayarlama
    fun cardViewStatus() {
        binding.cardViewSingUp.isVisible = false

        binding.tvSignView.setOnClickListener {
            binding.cardViewSingIn.isVisible = true
            binding.cardViewSingUp.isVisible = false
            binding.bttnAction.text = "Giriş Yap"
            Log.e("Visible singIna durumu","${binding.cardViewSingIn.visibility}")
            Log.e("Visible singuPa durumu","${binding.cardViewSingUp.visibility}")
        }


        binding.tvSignUpView.setOnClickListener {
            binding.cardViewSingIn.isVisible = false
            binding.cardViewSingUp.isVisible = true
            binding.bttnAction.text = "Kayıt Ol"
            Log.e("Visible singIn durumu","${binding.cardViewSingIn.visibility}")
            Log.e("Visible singUp durumu","${binding.cardViewSingUp.visibility}")
        }


    }




    fun alertMessage(a:Int){

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.error))
            .setMessage(resources.getString(a))

            .setPositiveButton(resources.getString(R.string.close)) { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }







}

