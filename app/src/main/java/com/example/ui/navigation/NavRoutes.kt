package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
  object Home : Screen("home", "Home")
  object Education : Screen("education", "Learn")
  object Videos : Screen("videos", "Videos")
  object DataBundle : Screen("data_bundle", "Fast Data")
  object Chat : Screen("chat", "Chat")
  object Calculator : Screen("calculator", "Calculator")
  object Creator : Screen("creator", "Creator Hub")
  object Profile : Screen("profile", "Profile")
}
