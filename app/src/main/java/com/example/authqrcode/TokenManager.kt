package com.example.authqrcode

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

object TokenManager {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    //private val COOKIE_KEY = stringPreferencesKey("auth_cookie")

    // Funcao para salvar o token e o cookie
    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            //prefs[COOKIE_KEY] = cookie
        }
    }

    // Funcao para pegar o token
    fun getToken(context: Context) : Flow<String> {
        return context.dataStore.data
            .map { prefs ->
                prefs[TOKEN_KEY] ?: ""
            }
            .catch { exception ->
                if (exception is IOException) {
                    // Lidar com exceções de IO, como por exemplo, logar ou emitir um valor padrão
                    emit("") // Ou outro valor padrão que faça sentido para seu app
                    //Log.e("TokenManager", "Erro ao ler DataStore", exception)
                } else {
                    throw exception // Re-lançar outras exceções
                }
            }
    }

    // Funcao para pegar o cookie
    /*fun getCookie(context: Context) : Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[COOKIE_KEY] ?: ""
        }
    }*/

    // Funcao caso precise de logout
    suspend fun clearData(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}