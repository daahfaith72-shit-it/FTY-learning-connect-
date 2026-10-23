package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark

// External link - easily updatable as required by project spec
const val FTY_FAST_DATA_URL = "https://dataplug-gh.com/pay/fty-fast-data-df037e81"

@Composable
fun DataBundleScreen(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  fun openDataPlugPurchase() {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(FTY_FAST_DATA_URL))
    context.startActivity(intent)
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("data_bundle_screen"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Title Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SkyBluePrimaryDark)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                Icons.Default.ElectricBolt,
                contentDescription = null,
                tint = Color(0xFFFACC15),
                modifier = Modifier.size(26.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "FTY Fast Data",
                style = MaterialTheme.typography.headlineSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White
                )
              )
              Text(
                text = "High-speed, affordable student internet packages",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFE0F2FE))
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Stay connected for online lessons, research, and video streams. Get affordable non-expiry data directly to your MTN, Telecel, or AT phone number.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f)),
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Primary CTA Button labelled "Buy Affordable Data"
          Button(
            onClick = { openDataPlugPurchase() },
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .testTag("buy_affordable_data_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
            shape = RoundedCornerShape(14.dp)
          ) {
            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Buy Affordable Data",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
              )
            )
          }
        }
      }
    }

    // Supported Networks Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.SignalCellularAlt, contentDescription = null, tint = SkyBluePrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Supported Networks in Ghana",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = SkyBluePrimaryDark
            )
          }
          Spacer(modifier = Modifier.height(12.dp))

          NetworkPill(
            network = "MTN Ghana",
            desc = "4G / 5G High Speed Data Bundles (Instant Delivery)",
            color = Color(0xFFEAB308)
          )
          Spacer(modifier = Modifier.height(8.dp))
          NetworkPill(
            network = "Telecel Ghana",
            desc = "Reliable bundles for school and research",
            color = Color(0xFFDC2626)
          )
          Spacer(modifier = Modifier.height(8.dp))
          NetworkPill(
            network = "AT (AirtelTigo)",
            desc = "Budget-friendly student packs",
            color = Color(0xFF2563EB)
          )
        }
      }
    }

    // How It Works Instructions
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SkyBlueExtraLight)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "How to Buy via DataPlug",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = SkyBluePrimaryDark
            )
          )
          Spacer(modifier = Modifier.height(10.dp))

          InstructionStep(number = "1", title = "Tap 'Buy Affordable Data'", desc = "Opens the official secure FTY Fast Data checkout page.")
          InstructionStep(number = "2", title = "Enter Recipient Phone Number", desc = "Provide the MTN, Telecel, or AT number to receive the bundle.")
          InstructionStep(number = "3", title = "Choose Bundle Size & Pay via MoMo", desc = "Authorize the payment prompt on your phone (MTN MoMo, Telecel Cash, or AT Money).")
          InstructionStep(number = "4", title = "Instant Confirmation", desc = "Data is credited directly to your SIM by the telecom network.")
        }
      }
    }

    // Transparency and Security Disclaimer (Rule: Do not claim purchase succeeded unless external service confirms)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            Icons.Default.Security,
            contentDescription = null,
            tint = Color(0xFFB45309),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Payment Security Notice",
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
              )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "FTY does not request, store, or process Mobile Money PINs in this application. All transactions are securely conducted on the DataPlug payment gateway. A data purchase is confirmed only after DataPlug and the telecom provider verify payment.",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF78350F)),
              lineHeight = 18.sp
            )
          }
        }
      }
    }

    // Bottom spacing
    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
private fun NetworkPill(network: String, desc: String, color: Color) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(SkyBlueLight, RoundedCornerShape(12.dp))
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(12.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = network,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        color = SkyBluePrimaryDark
      )
      Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569), fontSize = 11.sp)
      )
    }
  }
}

@Composable
private fun InstructionStep(number: String, title: String, desc: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.Top
  ) {
    Surface(
      shape = CircleShape,
      color = SkyBluePrimary,
      modifier = Modifier.size(24.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Text(
          text = number,
          color = Color.White,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
      }
    }
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = Color(0xFF0F172A)
      )
      Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
      )
    }
  }
}
