package com.kayna.spacescavenger.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GameScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Фон
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Color.Black)
        }
        // Корабль игрока

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, CircleShape)
                .align(Alignment.BottomCenter)
                .offset(y = -100.dp) // Поднимаем от низа
        )

        // Астероид (просто статичный серый квадрат сверху)
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.DarkGray, RectangleShape)
                .align(Alignment.TopStart)
                .offset(x = 100.dp, y = 50.dp)
        )

        // кнопка выхода назад в меню (для удобства тестирования)
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to menu")
        }
    }
}