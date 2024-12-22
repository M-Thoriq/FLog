import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.google.firebase.auth.FirebaseAuth
import com.thoriq.flog.R
import com.thoriq.flog.ui.theme.FlogTheme

@Composable
fun RegisterScreen(
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
    onRegisterSuccess: (Boolean, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var registrationStatus by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff001e2e)),
        contentAlignment = Alignment.BottomCenter,
    ) {
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
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 36.dp, vertical = 52.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
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
                text = "Register",
                color = Color(0xff000000),
                textAlign = TextAlign.Center,
            )
            // Text field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
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
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(text = "Confirm Password")
                },
                placeholder = {
                    Text(text = "Your Password")
                }
            )
            Text(
                text = registrationStatus,
            )

            // Button
            Button(
                onClick = {
                    if (password == confirmPassword) {
                        isLoading = true
                        registerWithFirebase(auth, email, password) { success, message ->
                            isLoading = false
                            registrationStatus =
                                if (success) "Registration successful!" else "Error: $message"
                            if (success) {
                                registrationStatus = "Register successful!"
                                onRegisterSuccess(
                                    true,
                                    "Register Ssuccessfully"
                                ) // Notify the parent of success
                            } else {
                                registrationStatus = "Error: $message"
                                onRegisterSuccess(false, "") // Notify the parent of failure
                            }
                        }
                    } else {
                        registrationStatus = "Passwords do not match."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Red)
                } else {
                    Text("Register")
                }
            }

            Text(modifier = Modifier.offset(y = (-16).dp).clickable {
                onRegisterSuccess(true, "")
            }, text = "Already have an account? Sign in", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

fun registerWithFirebase(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onResult: (Boolean, String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onResult(false, "Email and password cannot be empty.")
        return
    }

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "Success")

            } else {
                onResult(false, task.exception?.message ?: "Unknown error")
            }
        }
}