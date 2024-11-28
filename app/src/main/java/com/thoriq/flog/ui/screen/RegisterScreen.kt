import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(auth: FirebaseAuth = FirebaseAuth.getInstance(),onRegisterSuccess: (Boolean,String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var registrationStatus by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
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

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = {
                if (password == confirmPassword) {
                    isLoading = true
                    registerWithFirebase(auth, email, password) { success, message ->
                        isLoading = false
                        registrationStatus = if (success) "Registration successful!" else "Error: $message"
                        if (success) {
                            registrationStatus = "Register successful!"
                            onRegisterSuccess(true,"Register Ssuccessfully, Now Login ajg") // Notify the parent of success
                        } else {
                            registrationStatus = "Error: $message"
                            onRegisterSuccess(false,"") // Notify the parent of failure
                        }
                    }
                } else {
                    registrationStatus = "Passwords do not match."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Red)
            } else {
                Text("Register")
            }
        }

        Text(
            text = registrationStatus,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "have an account? Sign in",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    onRegisterSuccess(true,"")
                }
        )
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
