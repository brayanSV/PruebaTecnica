package com.user.brayan.pruebatecnica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.user.brayan.pruebatecnica.Model.ResObj
import com.user.brayan.pruebatecnica.Model.User
import com.user.brayan.pruebatecnica.Remote.ApiUtils
import com.user.brayan.pruebatecnica.Remote.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity(), View.OnClickListener {
    private var tilUser : TextInputLayout? = null
    private var tilPassword : TextInputLayout? = null
    private var btnLogin : Button? = null
    private var service : UserService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.tilUser = findViewById(R.id.tilUser)
        this.tilPassword = findViewById(R.id.tilPassword)
        this.btnLogin = findViewById(R.id.btnLogin)
        this.service = ApiUtils.getService()

        this.btnLogin?.setOnClickListener(this)
    }

    override fun onClick(view : View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                val user = User(tilUser?.editText?.text.toString(), tilPassword?.editText?.text.toString())

                if (validateLogin(user)) {
                    doLogin(user)
                }
            }
        }
    }

    private fun validateLogin(user : User) : Boolean {
        if (user.userName == null || user.userName!!.trim().length == 0) {
            Toast.makeText(this, R.string.user_login_error, Toast.LENGTH_SHORT)
            return false
        }

        if (user.password == null || user.password!!.trim().length == 0) {
            Toast.makeText(this, R.string.password_login_error, Toast.LENGTH_SHORT)
            return false
        }

        return true;
    }

    private fun doLogin(user : User) {
        val call = this.service?.login(user)
        call?.enqueue(object : Callback<ResObj> {
            override fun onResponse(call: Call<ResObj>, response: Response<ResObj>) {
                if (response.isSuccessful) {
                    val resObj : ResObj? = response.body()

                    val intent = Intent(this@Login, Menu::class.java).apply {
                        putExtra("expirationDate", resObj?.ExpirationDate)
                    }
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResObj>, t: Throwable) {
                Log.e("datos", t.message.toString())
            }
        })
    }


}