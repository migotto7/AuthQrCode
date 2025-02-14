package com.example.authqrcode

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //val dado = pickApiResponse()
        val email = intent.extras?.getString("email")
        val id = intent.extras?.getString("id")

        //Log.d("sendTokenToApi", "Dado: $dado")
        Log.d("HomeActivity", "Email: $email")
        Log.d("HomeActivity", "ID: $id")
    }
}

/*fun pickApiResponse() = runBlocking {
    launch {
        val cookie = "4cb17e7b-d061-4067-9ea9-fd94b54d5ded"
        val code = "e9b84535b91442348c7986bbb89832f9"
        val response = ApiClient.apiService.verifyDevice(cookie, code)
        if (response.isSuccessful) {
            // Handle successful response
            val data = response.body()
            Log.e("sendTokenToApi", "Sucesso ao pegar response")
        } else {
            // Handle error
            Log.e("sendTokenToApi", "Erro ao pegar response")
        }
    }
}*/