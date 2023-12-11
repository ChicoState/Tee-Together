package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tee_together.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // grab current instance of FirebaseAuth
        auth = FirebaseAuth.getInstance()
        // set a listener on the create account button
        binding.buttonCreateAccount.setOnClickListener {
            // grab the current values of each text field, trim any ending whitespace
            val displayName = binding.editTextDisplayName.text.toString().trim()
            val firstName = binding.editTextFirstName.text.toString().trim()
            val lastName = binding.editTextLastName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            // if no text fields empty, great and try to create a new user with the information given
            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && displayName.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    // if account created succesfully
                    if (task.isSuccessful) {
                        // get current user information
                        val user = auth.currentUser
                        // send a change request to firebase
                        val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build()
                        user?.updateProfile(userProfileChangeRequest)?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // After setting display name, save additional user information in Firestore
                                val userMap = hashMapOf(
                                    "displayName" to displayName,
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "email" to email
                                )
                                // Store user information to firebase, and go to profile page
                                FirebaseFirestore.getInstance().collection("users").document(user.uid).set(userMap)
                                    .addOnCompleteListener { firestoreTask ->
                                        if (firestoreTask.isSuccessful) {
                                            goToProfileActivity()
                                        } else {
                                            displayError(firestoreTask.exception)
                                        }
                                    }
                            } else {
                                displayError(updateTask.exception)
                            }
                        }
                    } else {
                        displayError(task.exception)
                    }
                }
            } else { // if not all fields valid, make Toast widget to inform user
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun goToProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()  // Finish LoginActivity so it's removed from the back stack
    }
    private fun displayError(exception: Exception?) {
        Toast.makeText(
            this,
            "Error: ${exception?.message}",
            Toast.LENGTH_LONG
        ).show()
    }
}
