package com.example.littlelemon

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yalantis.ucrop.UCrop
import java.io.File
import androidx.compose.runtime.setValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile (navController: NavHostController) {


    val context = LocalContext.current
    val viewModel = remember { ProfileViewModel(context) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = result.data?.let { UCrop.getOutput(it) }
            resultUri?.let {
                viewModel.onImageChange(it)

                scope.launch {
                    scale.snapTo(0.5f)
                    scale.animateTo(1f, animationSpec = tween(400))
                }
            }
        }
    }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val temp = createTempUri(context)
            cropImageLauncher.launch(
                UCrop.of(uri, temp)
                    .withAspectRatio(1f, 1f)
                    .getIntent(context)
            )
        }
    }
    val onPickImage = {
        pickImageLauncher.launch("image/*")
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF2F2F2)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    item{
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    item{
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item{
                        Box(
                            modifier = Modifier.size(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(viewModel.imageUri ?: R.drawable.profile)
                                    .crossfade(false)
                                    .build(),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(120.dp)
                                    .graphicsLayer {
                                        scaleX = scale.value
                                        scaleY = scale.value
                                    }
                                    .clip(CircleShape)
                                    .clickable(
                                    indication = rememberRipple(),
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                        onPickImage()
                                    },
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                                    .size(32.dp)
                                    .background(Color(0xFF495E57), CircleShape)
                                    .clickable(
                                        indication = rememberRipple(),
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        onPickImage()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.White)
                            }
                        }
                    }
                    item{
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item{
                        Text(
                            text = "Personal information",
                            color = Color(0xFF495E57),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    item{
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item{
                        OutlinedTextField(
                            value = viewModel.firstName,
                            onValueChange = { viewModel.onFirstNameChange(it) },
                            label = { Text("First name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item{
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item{
                        OutlinedTextField(
                            value = viewModel.lastName,
                            onValueChange = { viewModel.onLastNameChange(it) },
                            label = { Text("Last name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item{
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item{
                        OutlinedTextField(
                            value = viewModel.email ?: "",
                            onValueChange = {},
                            label = { Text("Email") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item{
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    item{
                        Button(
                            onClick = {
                                if (viewModel.firstName.isNotBlank() && viewModel.lastName.isNotBlank()) {
                                    viewModel.saveProfile()
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Profile saved")
                                    }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Please fill all fields")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF495E57)
                            )
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                    item{
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item{
                        Button(
                            onClick = {
                                viewModel.clearProfile()
                                navController.navigate(Destinations.Onboarding) {
                                    popUpTo(Destinations.Home) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF4CE14)
                            )
                        ) {
                            Text("Log out", color = Color.Black)
                        }
                    }
                }
            }
        }

    }
}

fun createTempUri(context: Context): Uri {
    val file = File(
        context.cacheDir,
        "temp_profile_${System.currentTimeMillis()}.jpg"
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Profile(navController)
}

