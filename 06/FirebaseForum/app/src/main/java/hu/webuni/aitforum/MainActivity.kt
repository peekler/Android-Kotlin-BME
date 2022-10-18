package hu.webuni.aitforum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hu.webuni.aitforum.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun loginClick(v: View) {
        if (!isFormValid()){
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            binding.etEmail.text.toString(), binding.etPassword.text.toString()
        ).addOnSuccessListener {
            Toast.makeText(this@MainActivity,
                "Login OK",
                Toast.LENGTH_LONG).show()

            // navigate to other Activity
            startActivity(Intent(this, ForumActivity::class.java))

        }.addOnFailureListener{
            Toast.makeText(this@MainActivity,
                "Error: ${it.message}",
                Toast.LENGTH_LONG).show()
        }

    }

    fun registerClick(v: View){
        if (!isFormValid()){
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.etEmail.text.toString(), binding.etPassword.text.toString()
        ).addOnSuccessListener {
            Toast.makeText(this@MainActivity,
                "Registration OK",
                Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(this@MainActivity,
                "Error: ${it.message}",
                Toast.LENGTH_LONG).show()
        }
    }

    fun isFormValid(): Boolean {
        return when {
            binding.etEmail.text.isEmpty() -> {
                binding.etEmail.error = "This field can not be empty"
                false
            }
            binding.etPassword.text.isEmpty() -> {
                binding.etPassword.error = "The password can not be empty"
                false
            }
            else -> true
        }
    }




}