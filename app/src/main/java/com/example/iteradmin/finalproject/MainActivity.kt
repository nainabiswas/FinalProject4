package com.example.iteradmin.finalproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
private lateinit var client:GoogleApiClient
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth= FirebaseAuth.getInstance()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        client= GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build()

        val signin =findViewById<SignInButton>(R.id.signin)
        signin.setOnClickListener {
            signin()

        }
    }
    private fun signin(){
        val i= Auth.GoogleSignInApi.getSignInIntent(client)
        startActivityForResult(i,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthwithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
                // ...
            }

        }
    }

    private fun firebaseAuthwithGoogle(result: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(result?.idToken,null)

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) {
                    task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"signin successfully...",Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this,"signin error...",Toast.LENGTH_LONG).show()
                    }
                }
    }

}
