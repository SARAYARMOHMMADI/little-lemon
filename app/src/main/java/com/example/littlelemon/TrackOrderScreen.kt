package com.example.littlelemon

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.animation.core.tween
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

enum class OrderStatus {
    PREPARING,
    ON_THE_WAY,
    DELIVERED
}

@Composable
fun TrackOrderScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {

    var orderStatus by remember { mutableStateOf(OrderStatus.PREPARING) }

    LaunchedEffect(Unit) {
        delay(3000)
        orderStatus = OrderStatus.ON_THE_WAY

        delay(50000)
        orderStatus = OrderStatus.DELIVERED
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // 🔹 Header
        TrackHeader(navController)

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Your order is on the way!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Estimated delivery: 20 minutes",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("Items: ${cartViewModel.cartItems.size}",
                color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedOrderProgress(status = orderStatus)

            Spacer(modifier = Modifier.height(40.dp))


            Button(
                onClick = {
                    cartViewModel.clearCart()
                    navController.popBackStack(Destinations.Home, false)
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF4CE14),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Back to Home", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TrackHeader(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {


        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF495E57)
            )
        }


        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AnimatedOrderProgress(status: OrderStatus) {

    val targetProgress = when (status) {
        OrderStatus.PREPARING -> 0.33f
        OrderStatus.ON_THE_WAY -> 0.66f
        OrderStatus.DELIVERED -> 1f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "order_progress"
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = Color(0xFF495E57),
            trackColor = Color(0xFFE0E0E0)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProgressStepLabel(
                text = "Preparing",
                isActive = status == OrderStatus.PREPARING ||
                        status == OrderStatus.ON_THE_WAY ||
                        status == OrderStatus.DELIVERED
            )

            ProgressStepLabel(
                text = "On the way",
                isActive = status == OrderStatus.ON_THE_WAY ||
                        status == OrderStatus.DELIVERED
            )

            ProgressStepLabel(
                text = "Delivered",
                isActive = status == OrderStatus.DELIVERED
            )
        }
    }
}

@Composable
fun ProgressStepLabel(text: String, isActive: Boolean) {
    Text(
        text = text,
        color = if (isActive) Color(0xFF495E57) else Color.Gray,
        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
        fontSize = 12.sp
    )
}
