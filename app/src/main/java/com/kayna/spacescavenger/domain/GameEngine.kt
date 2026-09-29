package com.kayna.spacescavenger.domain

import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

// Константы скорости (в пикселях за кадр/обновление)
private const val ASTEROID_SPEED = 5f
private const val PLAYER_SPEED = 8f

class GameEngine(
    private val screenWidth: Float,
    private val screenHeight: Float
) {
    // Координта игрока (начинаем в центре снизу)
    var playerPosition = Offset(screenWidth / 2, screenHeight - 100f)
        private set

    // Координаты астероида (появляется справа за экраном)
    var asteroidPosition = Offset(screenWidth + 50f, 100f)
        private set

    fun updatePlayer(targetX: Float) {
        // Плавное движение игрока к точке касания
        val currentX = playerPosition.x
        if (abs(currentX - targetX) > PLAYER_SPEED) {
            playerPosition = if (currentX < targetX) {
                playerPosition.copy(x = currentX + PLAYER_SPEED)
            } else {
                playerPosition.copy(x = currentX - PLAYER_SPEED)
            }
        } else {
            playerPosition = playerPosition.copy(x = targetX)
        }
    }

    fun updateAsteroid() {
        // Двигаем астероид влево
        asteroidPosition = asteroidPosition.copy(x = asteroidPosition.x - ASTEROID_SPEED)
    }

    fun isOffScreen(): Boolean {
        return asteroidPosition.x < -50f // если ушел левее левого края
    }

    fun resetAsteroid() {
        // перезапускаем астероид справа со случайной высотой
        asteroidPosition = Offset(screenWidth + 50f, (0..screenHeight.toInt()).random().toFloat())
    }

    fun checkCollision(shipSize: Float, asteroidSize: Float): Boolean {
        // простейшая проверка столкновения прямоугольников (Bounding Box)
        val shipRect = RectF(
            playerPosition.x - shipSize / 2,
            playerPosition.y - shipSize / 2,
            playerPosition.x + shipSize / 2,
            playerPosition.y +shipSize / 2
        )
        val asteroidRect = RectF(
            asteroidPosition.x,
            asteroidPosition.y,
            asteroidPosition.x + asteroidSize,
            asteroidPosition.y + asteroidSize
        )
        return shipRect.intersect(asteroidRect)
    }
}