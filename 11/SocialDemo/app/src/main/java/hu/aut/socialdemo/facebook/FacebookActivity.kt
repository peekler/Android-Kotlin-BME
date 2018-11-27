package hu.aut.socialdemo.facebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import hu.aut.socialdemo.R
import java.util.*
import android.content.Intent
import com.facebook.*


class FacebookActivity : AppCompatActivity() {

    private val callbackManager = CallbackManager.Factory.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook)

        login_button.setReadPermissions(Arrays.asList("email", "public_profile"))


        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    val accessToken = AccessToken.getCurrentAccessToken()
                    val isLoggedIn = accessToken != null && !accessToken.isExpired
                    val paramsBundle = Bundle()

                    GraphRequest(accessToken,
                        "/me",
                        paramsBundle,
                        HttpMethod.GET,
                        GraphRequest.Callback {
                            //handle GraphResponse
                        }).executeAsync()

                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
