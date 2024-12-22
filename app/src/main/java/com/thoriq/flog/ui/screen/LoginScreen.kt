package com.thoriq.flog.ui.screen

import RegisterScreen
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thoriq.flog.R
import com.thoriq.flog.repository.Login
import com.thoriq.flog.repository.WeatherRepository
import com.thoriq.flog.ui.theme.FlogTheme

@Composable
fun LoginScreen(auth: FirebaseAuth = FirebaseAuth.getInstance(), onLoginSuccess: (Boolean,String) -> Unit) {
    val context = LocalContext.current
    val Login = Login()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginStatus by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var regis by remember { mutableStateOf(false) }
    var regisMessage by remember { mutableStateOf("") }
    val isLoggedIn = remember { Login.getLoginState(context) }
    val loggedInEmail = remember { Login.getUserEmail(context) }

    // Basic UI elements for the static login screen


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff001e2e)),
        contentAlignment = Alignment.BottomCenter,
    ) {
        if(isLoggedIn){
            if (loggedInEmail != null) {
                onLoginSuccess(true,loggedInEmail)
            }
        }
        else if (!regis) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart,
            ) {
                Image(painter = painterResource(id = R.drawable.flogwp), contentDescription = "", contentScale = ContentScale.Fit)
                Row(
                    modifier = Modifier.padding(16.dp),
                ){
                    Image(painter = painterResource(id = R.drawable.ic_flog), contentDescription = "", modifier = Modifier.size(36.dp))
                    Text(
                        text = "Flog",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            lineHeight = 32.sp,
                            letterSpacing = 0.15.sp,
                            color = Color(0xffffffff),
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()

                    .background(
                        Color(0xffffffff),
                        RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(horizontal = 36.dp, vertical = 52.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                // Login

                Text(
                    style =
                    // Poppins / SemiBold
                    TextStyle(
                        fontWeight = FontWeight(600),
                        fontSize = 36.sp,
                        lineHeight = 16.sp,
                    ),
                    text = "Login",
                    color = Color(0xff000000),
                    textAlign = TextAlign.Center,
                )
                // Text field
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = { username = it },
                    label = {
                        Text(text = "Email")
                    },
                    placeholder = {
                        Text(text = "example@flog.com")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(text = "Password")
                    },
                    placeholder = {
                        Text(text = "Your Password")
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Text(
                    text = loginStatus,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                // Button
                Button(
                    onClick = {
                        isLoading = true
                        loginWithFirebase(auth, username, password) { success, message ->
                            isLoading = false
                            if (success) {
                                Login.saveLoginState(context, true, username)
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

                Text(modifier = Modifier.offset(y = (-16).dp).clickable {
                            // Navigate to RegisterScreen directly when clicked
                            loginStatus = ""
                            regis = true // This will navigate to the Register screen

                        }, text = "Don't have an account? Sign up", color = MaterialTheme.colorScheme.secondary)
            }
        }

        else{
            RegisterScreen(auth = auth){success,message->
                if (success) {
                    regis = false
                    if ((message != ""))
                    {
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
                    }

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