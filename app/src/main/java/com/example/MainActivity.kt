package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.navigation.FtyApp
import com.example.ui.theme.BackgroundWhite
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FtyViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: FtyViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(darkTheme = false) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = BackgroundWhite
        ) {
          FtyApp(viewModel = viewModel)
        }
      }
    }
  }
}
