package com.thoriq.flog.ui.screen

import RegisterScreen
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "user_prefs"
private const val KEY_IS_LOGGED_IN = "is_logged_in"
private const val KEY_USER_EMAIL = "user_email"

fun saveLoginState(context: Context, isLoggedIn: Boolean, email: String) {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    editor.putString(KEY_USER_EMAIL, email)
    editor.apply()  // or editor.commit() if you want synchronous storage
}

fun getLoginState(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
}

fun getUserEmail(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(KEY_USER_EMAIL, null)
}


@Composable
fun LoginScreen(auth: FirebaseAuth = FirebaseAuth.getInstance(), onLoginSuccess: (Boolean,String) -> Unit) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginStatus by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var regis by remember { mutableStateOf(false) }
    var regisMessage by remember { mutableStateOf("") }
    val isLoggedIn = remember { getLoginState(context) }
    val loggedInEmail = remember { getUserEmail(context) }

    // Basic UI elements for the static login screen


    Box {
        if(isLoggedIn){
            onLoginSuccess(true,username)
        }
        else if (!regis) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = regisMessage,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Button(
                    onClick = {
                        isLoading = true
                        loginWithFirebase(auth, username, password) { success, message ->
                            isLoading = false
                            if (success) {
                                saveLoginState(context, true, username)
                                Toast.makeText(context,"Login SuckSeed", Toast.LENGTH_SHORT).show()
                                onLoginSuccess(true,username) // Notify the parent of success
                            } else {
                                loginStatus = "Error: $message"
                                onLoginSuccess(false,"") // Notify the parent of failure
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Red)
                    } else {
                        Text("Login")
                    }
                }

                Text(
                    text = loginStatus,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Red
                )

                Text(
                    text = "Don't have an account? Sign up",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            // Navigate to RegisterScreen directly when clicked
                            loginStatus = ""
                            regis = true // This will navigate to the Register screen

                        }
                )
            }
        }

        else{
            RegisterScreen(auth = auth){success,message->
                if (success) {
                    regis = false
                    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()

                }

            }

        }        }
}

fun loginWithFirebase(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onResult: (Boolean, String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onResult(false, "Email and password cannot be empty")
        return
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "Success")
            } else {
                onResult(false, task.exception?.message ?: "Unknown error")
            }
        }
}















//@Preview
//@Composable
//private fun MapsPrev() {
//    LoginScreen()
//}