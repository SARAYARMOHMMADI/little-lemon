package com.example.littlelemon

import android.app.Activity.RESULT_OK
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.saveable.rememberSaveable
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Onboarding(navController: NavHostController) {

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)
    val savedPath = sharedPref.getString("profileImage", null)

    var imageUri by rememberSaveable  {
        mutableStateOf(
            savedPath?.let { Uri.fromFile(File(it)) }
        )
    }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {

            val resultUri = result.data?.let { UCrop.getOutput(it) } ?: return@rememberLauncherForActivityResult

            resultUri.let {
                val savedPath = saveImageToInternalStorage(context, it)

                sharedPref.edit()
                    .putString("profileImage", savedPath)
                    .apply()

                imageUri = Uri.fromFile(File(savedPath))
            }
        }
    }

    val pickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        uri?.let {

            val destinationUri = Uri.fromFile(
                File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
            )

            val uCrop = UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(500, 500)

            val intent = uCrop.getIntent(context)

            cropLauncher.launch(intent)
        }
    }


    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable  { mutableStateOf("") }
    var email by rememberSaveable  { mutableStateOf("") }
    var message by rememberSaveable  { mutableStateOf("") }


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF4F4F4)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF4F4F4)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(45.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Green header bar (full width)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF495E57))
                        .padding(vertical = 40.dp),

                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Let's get to know you",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Card container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Personal information",
                        color = Color(0xFF495E57),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Box {

                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .clickable {
                                        pickLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {

                                if (imageUri != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUri),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop // 👈 مهم
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.profile),
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF495E57))
                                    .clickable {
                                        pickLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "First name",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },

                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Last name",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        //label = { Text("Last name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        //label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                                message = "Registration unsuccessful. Please enter all data."
                            }
                            else if( !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                message = "Please enter valid data."
                            }
                            else {
                                sharedPref.edit()
                                    .putString("firstName", firstName)
                                    .putString("lastName", lastName)
                                    .putString("email", email)
                                    .apply()

                                message = "Registration successful!"

                                navController.navigate(Destinations.Home) {
                                    popUpTo(Destinations.Onboarding) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF4CE14)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFEE9972)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text("Register", color = Color.Black)
                    }
                    val isError = message.startsWith("Registration unsuccessful") || message.startsWith("Please")

                    if (message.isNotEmpty()) {
                        Text(
                            message,
                            color = if (isError) Color.Red else Color(0xFF4CAF50)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    val navController = rememberNavController()
    Onboarding(navController)
}

fun saveImageToInternalStorage(context: Context, uri: Uri): String {

    val fileName = "profile_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)

    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }

    return file.absolutePath
}