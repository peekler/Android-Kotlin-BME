package hu.aut.socialdemo.google

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import hu.aut.socialdemo.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_google.*


class GoogleActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 112
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            textView2.text = account.toJson()

        } catch (t: Exception) {
            t.printStackTrace()
            textView2.text = "Sign in error"
        }
    }

}
