package com.example.authqrcode

import android.util.Log
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/notary__device_verify_api")
    suspend fun verifyDevice(
        @Query("cookie") cookie: String,
        @Query("code") code: String
    ): Response<Void> // Replace YourResponseType with your actual response type

    @POST("/code")
    suspend fun sendToken(
        @Header("Authorization") token: String
        // Send the token in the Authorization header
    ): Response<Void>

    /*@GET("/your/api/userinfo")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
        // Send the token in the Authorization header
    ): Response<UserInfoResponse> // Replace with your response type*/
}

/*data class UserInfoResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)*/

class CookieInterceptor(private val cookie: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request: Request = chain.request().newBuilder()
            .addHeader("cookie", cookie)
            .build()
        return chain.proceed(request)
    }
}

object ApiClient {
    private const val BASE_URL = "https://0d499ba910e946cb.2o2.company/api/"

    fun create(cookie: String): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(CookieInterceptor(cookie))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

suspend fun sendTokenToApi(token: String, cookie: String) {
    val apiService = ApiClient.create(cookie)
    withContext(Dispatchers.IO) {
        try {
            val sendTokenResponse = apiService.sendToken("Bearer $token")
            Log.d("token", token)
            if(sendTokenResponse.isSuccessful){
                Log.d("sendTokenToApi", "Token sent successfully")

                // Fetch user info after successfully sending the token
                /*val userInfoResponse = ApiClient.apiService.getUserInfo("Bearer $token")
                if (userInfoResponse.isSuccessful) {
                    val userInfo = userInfoResponse.body()
                    Log.d("sendTokenToApi", "User Info: $userInfo")
                } else {
                    Log.e("sendTokenToApi", "Error getting user info: ${userInfoResponse.code()}")
                }*/
            }else{
                Log.e("sendTokenToApi", "Error sending token: ${sendTokenResponse.code()}")
            }
        } catch (e: Exception) {
            Log.e("sendTokenToApi", "Exception sending token", e)
        }
    }
}