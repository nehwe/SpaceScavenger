package com.kayna.spacescavenger.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kayna.spacescavenger.domain.GameEngine
import kotlinx.coroutines.android.awaitFrame

@Composable
fun GameScreen(navController: NavController) {

    // конфигурация размеров
    val shipSizeDp = 50.dp
    val asteroidSizeDp = 60.dp
    val density = LocalDensity.current

    // конвертируем dp в пиксели один раз при старте игры
    val shipSizePx = with(density) { shipSizeDp.toPx() }
    val asteroidPx = with(density) { asteroidSizeDp.toPx() }

    // состояние движка игры
    var screenWidth by remember { mutableStateOf(0f) }
    var screenHeight by remember { mutableStateOf(0f) }
    var gameEngine by remember { mutableStateOf<GameEngine?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned  { coordinates ->
            // Compose сам передает нам точные размеры экрана сюда
            screenWidth = coordinates.size.width.toFloat()
            screenHeight = coordinates.size.height.toFloat()
        }
    ) {
        // Фон
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Color.Black)
        }

        // Корабль игрока
        Box(
            modifier = Modifier
                .size(shipSizeDp)
                .background(Color.White, CircleShape)
                .offset {
                    // читаем позицию из движка
                    IntOffset(
                        x = (gameEngine?.playerPosition?.x?.minus(shipSizePx / 2)?.toInt() ?: 0),
                        y = (gameEngine?.playerPosition?.y?.minus(shipSizePx / 2)?.toInt() ?: 0)
                    )
                }
        )

        // Астероид (просто статичный серый квадрат сверху)
        Box(
            modifier = Modifier
                .size(asteroidSizeDp)
                .background(Color.DarkGray, RectangleShape)
                .offset {
                    IntOffset(
                        x = (gameEngine?.asteroidPosition?.x?.toInt() ?: 0),
                        y = (gameEngine?.asteroidPosition?.y?.toInt() ?: 0)
                    )
                }
        )

        // Текст проигрыша
        if (isGameOver) {
            Text("GAME OVER", fontSize = 40.sp, color = Color.Red, modifier = Modifier.align(Alignment.Center))
        }

        // кнопка выхода назад в меню (для удобства тестирования)
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }

    // жизненный цикл экрана: инициализация и игровой цикл
    LaunchedEffect(key1 = screenWidth) {
        // Получаем реальные размеры экрана через OnGloballyPositionedListener
        if (screenWidth > 0) {

            gameEngine = GameEngine(screenWidth, screenHeight)

            while (true) {
                awaitFrame() // синхронизация с частотой обновления монитора (~60 FPS)

                val engine = gameEngine ?: continue

                // обновляем логику
                engine.updateAsteroid()

                // проверяем выход за экран
                if (engine.isOffScreen()) {
                    engine.resetAsteroid()
                }

                // проверка столкновения
                if (engine.checkCollision(shipSizePx, asteroidPx)) {
                    isGameOver = true
                    break // останавливаем цикл while(true)
                }
            }
        }
    }

    // обработка свайпов/движения пальца
    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                if (!isGameOver) {
                    change.consume()
                    gameEngine?.let { engine -> // Мы берем текущую цель из движения пальца
                        val targetX = engine.playerPosition.x + dragAmount.x // Ограничиваем движение, чтобы корабль не ушел за края
                    val boundedX = targetX.coerceIn(
                        shipSizePx / 2,
                        screenWidth - shipSizePx / 2 // используем screenWidth из Composable
                    )
                        engine.updatePlayer(boundedX) // Передаем в движок
                    }
                }
            }
        }
    )

}