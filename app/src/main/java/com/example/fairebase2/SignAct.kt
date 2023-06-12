package com.example.fairebase2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.fairebase2.databinding.ActivitySignBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignAct : AppCompatActivity() {
    lateinit var binding: ActivitySignBinding
//    lateinit var signInRequest: BeginSignInRequest
//    private lateinit var oneTapClient: SignInClient
    lateinit var launcher: ActivityResultLauncher<Intent>
    
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var auth: FirebaseAuth

//dddddd
    override fun onCreate(savedInstanceState: Bundle?) {


        binding = ActivitySignBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        launcher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account!=null){
                        firebaseAuthWithGoogle(account.idToken!!)
                    }

                } catch (e: ApiException){


                    Log.d("myLog","ApiException")
                }
        }
        binding.button2.setOnClickListener{
            signInEP(binding.editTextTextEmailAddress.text.toString(),binding.editTextTextEmailAddress2.text.toString())
        }



        binding.googleButton.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            launcher.launch(googleSignInClient.signInIntent)
        }


        checkAuthState()
    }




    private fun firebaseAuthWithGoogle(idToken:String){
        val credential  = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("myLog","google sig in success")
                checkAuthState()
            }else{
                Log.d("myLog","google sig in error")
            }
        }

    }

    private fun checkAuthState(){
        if(auth.currentUser!= null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun signInEP(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }


    private fun updateUI(user: FirebaseUser?) {
        if(user!= null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

    }

    companion object {
        private const val TAG = "EmailPassword"
    }




    }
