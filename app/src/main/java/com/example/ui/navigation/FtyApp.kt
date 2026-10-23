package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CreatorDashboardScreen
import com.example.ui.screens.DataBundleScreen
import com.example.ui.screens.EducationScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.VideoFeedScreen
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FtyApp(
  viewModel: FtyViewModel
) {
  var currentRoute by remember { mutableStateOf(Screen.Home.route) }
  val user by viewModel.currentUser.collectAsStateWithLifecycle()

  // Handle system back navigation
  if (currentRoute != Screen.Home.route) {
    BackHandler {
      currentRoute = Screen.Home.route
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "FTY",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = SkyBluePrimary
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = when (currentRoute) {
                Screen.Home.route -> "• Tembilla Faith Yenpang"
                Screen.Education.route -> "• Learning Hub"
                Screen.DataBundle.route -> "• Fast Data"
                Screen.Videos.route -> "• Shorts"
                Screen.Chat.route -> "• Community Chat"
                Screen.Calculator.route -> "• Calculator"
                Screen.Creator.route -> "• Creator Studio"
                Screen.Profile.route -> "• Profile"
                else -> ""
              },
              style = MaterialTheme.typography.titleSmall.copy(
                color = SkyBluePrimaryDark,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        },
        actions = {
          // Calculator shortcut icon in top bar
          IconButton(
            onClick = {
              currentRoute = if (currentRoute == Screen.Calculator.route) Screen.Home.route else Screen.Calculator.route
            },
            modifier = Modifier.testTag("topbar_calculator_btn")
          ) {
            Icon(
              Icons.Default.Calculate,
              contentDescription = "Calculator",
              tint = if (currentRoute == Screen.Calculator.route) SkyBluePrimary else Color(0xFF64748B)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier
          .navigationBarsPadding()
          .testTag("bottom_nav_bar"),
        containerColor = Color.White,
        tonalElevation = 6.dp
      ) {
        val navItems = listOf(
          Triple(Screen.Home.route, "Home", Icons.Default.Home),
          Triple(Screen.Education.route, "Learn", Icons.Default.School),
          Triple(Screen.DataBundle.route, "Fast Data", Icons.Default.ElectricBolt),
          Triple(Screen.Videos.route, "Videos", Icons.Default.VideoLibrary),
          Triple(Screen.Chat.route, "Chat", Icons.Default.Forum),
          Triple(Screen.Creator.route, "Creator", Icons.Default.MonetizationOn),
          Triple(Screen.Profile.route, "Profile", Icons.Default.Person)
        )

        navItems.forEach { (route, label, icon) ->
          val isSelected = currentRoute == route
          NavigationBarItem(
            selected = isSelected,
            onClick = { currentRoute = route },
            icon = {
              Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(22.dp)
              )
            },
            label = {
              Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = SkyBluePrimaryDark,
              selectedTextColor = SkyBluePrimaryDark,
              indicatorColor = SkyBlueLight,
              unselectedIconColor = Color(0xFF94A3B8),
              unselectedTextColor = Color(0xFF94A3B8)
            ),
            modifier = Modifier.testTag("nav_item_$route")
          )
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (currentRoute) {
        Screen.Home.route -> {
          HomeScreen(
            viewModel = viewModel,
            onNavigate = { target -> currentRoute = target }
          )
        }
        Screen.Education.route -> {
          EducationScreen(
            viewModel = viewModel,
            onOpenCalculator = { currentRoute = Screen.Calculator.route }
          )
        }
        Screen.DataBundle.route -> {
          DataBundleScreen()
        }
        Screen.Videos.route -> {
          VideoFeedScreen(viewModel = viewModel)
        }
        Screen.Chat.route -> {
          ChatScreen(viewModel = viewModel)
        }
        Screen.Calculator.route -> {
          CalculatorScreen(viewModel = viewModel)
        }
        Screen.Creator.route -> {
          CreatorDashboardScreen(viewModel = viewModel)
        }
        Screen.Profile.route -> {
          ProfileScreen(
            viewModel = viewModel,
            onOpenCreatorHub = { currentRoute = Screen.Creator.route }
          )
        }
      }
    }
  }
}
