package com.kayna.spacescavenger.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.kayna.spacescavenger.navigation.NavRoutes

@Composable
fun MenuScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Космический мусорщик", fontSize = 24.sp, modifier = Modifier.padding(20.dp))

        Button(onClick = {
            // При нажатии говорим навигации перейти на экран ГЕЙМ
            navController.navigate(NavRoutes.GAME)
        }) {
            Text("Играть")
        }

        Button(onClick = { /* пока оставим пустым */ }) {
            Text("Рекорды")
        }
    }
}