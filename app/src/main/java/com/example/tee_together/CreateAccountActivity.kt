package com.example.tee_together

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tee_together.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.buttonCreateAccount.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString().trim()
            val lastName = binding.editTextLastName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        val userMap = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "email" to email
                        )

                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").document(userId).set(userMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Account and User Data Created Successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
