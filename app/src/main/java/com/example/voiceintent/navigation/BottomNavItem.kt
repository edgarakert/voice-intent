package com.example.voiceintent.navigation

import androidx.annotation.DrawableRes
import com.example.voiceintent.R

sealed class BottomNavItem(
    val screen: Screen,
    val label: String,
    @param:DrawableRes val iconRes: Int
) {
    object Notes : BottomNavItem(
        screen = Screen.Notes,
        label = "Заметки",
        iconRes = R.drawable.ic_outline_sticky_note_24
    )

    object Analytics : BottomNavItem(
        screen = Screen.Analytics,
        label = "Аналитика",
        iconRes = R.drawable.ic_outline_analytics_24
    )

    object Settings : BottomNavItem(
        screen = Screen.Settings,
        label = "Настройки",
        iconRes = R.drawable.ic_outline_settings_24
    )

    companion object {
        val items = listOf(Notes, Analytics, Settings)
    }
}