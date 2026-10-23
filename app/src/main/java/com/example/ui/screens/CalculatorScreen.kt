package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel

@Composable
fun CalculatorScreen(
  viewModel: FtyViewModel,
  modifier: Modifier = Modifier
) {
  val expression by viewModel.calcExpression.collectAsStateWithLifecycle()
  val result by viewModel.calcResult.collectAsStateWithLifecycle()
  val isDegree by viewModel.isDegree.collectAsStateWithLifecycle()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp)
      .testTag("calculator_screen"),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Calculator Display Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(bottom = 12.dp)
        .testTag("calc_display_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "FTY Scientific Calculator",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = SkyBluePrimaryDark
            )
          )

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isDegree) SkyBlueLight else Color(0xFFFEF3C7),
            modifier = Modifier.clickable { viewModel.onCalcInput("DEG/RAD") }
          ) {
            Text(
              text = if (isDegree) "DEG" else "RAD",
              color = if (isDegree) SkyBluePrimaryDark else Color(0xFFB45309),
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.End
        ) {
          Text(
            text = expression.ifEmpty { "0" },
            style = MaterialTheme.typography.headlineSmall.copy(
              color = if (expression.isEmpty()) Color(0xFF94A3B8) else Color(0xFF334155),
              fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.End,
            maxLines = 2
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = result,
            style = MaterialTheme.typography.headlineLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              color = SkyBluePrimary
            ),
            textAlign = TextAlign.End
          )
        }
      }
    }

    // Keypad Grid
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 64.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Row 1: Scientific functions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalcButton("sin", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("sin") }
        CalcButton("cos", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("cos") }
        CalcButton("tan", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("tan") }
        CalcButton("ln", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("ln") }
        CalcButton("log", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("log") }
      }

      // Row 2: Powers, Roots, Constants
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalcButton("√", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("√") }
        CalcButton("^", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("^") }
        CalcButton("π", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("π") }
        CalcButton("e", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("e") }
        CalcButton("C", Modifier.weight(1f), isAction = true) { viewModel.onCalcInput("C") }
      }

      // Row 3: Parentheses & Operations
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalcButton("(", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("(") }
        CalcButton(")", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput(")") }
        CalcButton("%", Modifier.weight(1f), isFunc = true) { viewModel.onCalcInput("%") }
        CalcButton("⌫", Modifier.weight(1f), isAction = true) { viewModel.onCalcInput("⌫") }
        CalcButton("÷", Modifier.weight(1f), isOp = true) { viewModel.onCalcInput("÷") }
      }

      // Row 4: 7, 8, 9, ×
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalcButton("7", Modifier.weight(1f)) { viewModel.onCalcInput("7") }
        CalcButton("8", Modifier.weight(1f)) { viewModel.onCalcInput("8") }
        CalcButton("9", Modifier.weight(1f)) { viewModel.onCalcInput("9") }
        CalcButton("×", Modifier.weight(1f), isOp = true) { viewModel.onCalcInput("×") }
      }

      // Row 5: 4, 5, 6, -
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalcButton("4", Modifier.weight(1f)) { viewModel.onCalcInput("4") }
        CalcButton("5", Modifier.weight(1f)) { viewModel.onCalcInput("5") }
        CalcButton("6", Modifier.weight(1f)) { viewModel.onCalcInput("6") }
        CalcButton("-", Modifier.weight(1f), isOp = true) { viewModel.onCalcInput("-") }
      }

      // Row 6: 1, 2, 3, +
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalcButton("1", Modifier.weight(1f)) { viewModel.onCalcInput("1") }
        CalcButton("2", Modifier.weight(1f)) { viewModel.onCalcInput("2") }
        CalcButton("3", Modifier.weight(1f)) { viewModel.onCalcInput("3") }
        CalcButton("+", Modifier.weight(1f), isOp = true) { viewModel.onCalcInput("+") }
      }

      // Row 7: 0, ., =
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalcButton("0", Modifier.weight(1.5f)) { viewModel.onCalcInput("0") }
        CalcButton(".", Modifier.weight(1f)) { viewModel.onCalcInput(".") }
        CalcButton("=", Modifier.weight(1.5f), isEquals = true) { viewModel.onCalcInput("=") }
      }
    }
  }
}

@Composable
private fun CalcButton(
  text: String,
  modifier: Modifier = Modifier,
  isFunc: Boolean = false,
  isOp: Boolean = false,
  isAction: Boolean = false,
  isEquals: Boolean = false,
  onClick: () -> Unit
) {
  val bgColor = when {
    isEquals -> SkyBluePrimary
    isAction -> Color(0xFFEF4444).copy(alpha = 0.12f)
    isOp -> SkyBlueLight
    isFunc -> SkyBlueExtraLight
    else -> Color.White
  }

  val textColor = when {
    isEquals -> Color.White
    isAction -> Color(0xFFDC2626)
    isOp -> SkyBluePrimaryDark
    isFunc -> SkyBluePrimaryDark
    else -> Color(0xFF0F172A)
  }

  Box(
    modifier = modifier
      .height(50.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .clickable(onClick = onClick)
      .testTag("calc_btn_$text"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      color = textColor,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = if (isEquals || isOp || isAction) FontWeight.Bold else FontWeight.SemiBold,
        fontSize = if (isFunc) 14.sp else 18.sp
      )
    )
  }
}
