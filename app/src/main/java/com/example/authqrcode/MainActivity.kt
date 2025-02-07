package com.example.authqrcode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val token = TokenManager.getToken(this@MainActivity).first()
            if(token.isNotEmpty()){
                navigateToHome()
            }else{
                // Configurar a interface se necessario
                findViewById<Button>(R.id.btnScanQr).setOnClickListener {
                    navigateToScanner()
                }
            }
        }
    }

    // Funcão de navegação para a tela após ler o token ou ja estar autenticado
    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    // Função de navegação para a tela de scanner
    private fun navigateToScanner(){
        startActivity(Intent(this, QrCodeScannerActivity::class.java))
    }
}