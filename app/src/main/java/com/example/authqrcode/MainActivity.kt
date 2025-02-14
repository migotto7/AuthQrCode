package com.example.authqrcode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(isBiometricAvailable()){
            showBiometricPrompt()
        }else{
            checkAuthenticaion()
        }
    }

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun showBiometricPrompt() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                checkAuthenticaion()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                findViewById<Button>(R.id.btnScanQr).text = "Tente novamente"
                findViewById<Button>(R.id.btnScanQr).setOnClickListener {
                    navigateToScanner()
                }
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação Biométrica")
            .setSubtitle("Use sua biometria para acessar o aplicativo")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun checkAuthenticaion() {
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