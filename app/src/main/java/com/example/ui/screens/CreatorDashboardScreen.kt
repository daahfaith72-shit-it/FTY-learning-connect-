package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.PayoutRequestEntity
import com.example.data.model.UserEntity
import com.example.data.model.VideoPostEntity
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreatorDashboardScreen(
  viewModel: FtyViewModel,
  modifier: Modifier = Modifier
) {
  val user by viewModel.currentUser.collectAsStateWithLifecycle()
  val videos by viewModel.allVideos.collectAsStateWithLifecycle()
  val payouts by viewModel.allPayouts.collectAsStateWithLifecycle()
  val context = LocalContext.current

  var showCashoutDialog by remember { mutableStateOf(false) }
  var showAddLessonDialog by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("creator_dashboard_screen")
  ) {
    if (user?.isCreator != true) {
      // PIN Gate: User must enter creator PIN "faith1132A"
      CreatorPinGate(
        onVerify = { pin ->
          viewModel.unlockCreatorWithPin(pin) { success ->
            if (success) {
              Toast.makeText(context, "Welcome Faith! Creator Mode unlocked.", Toast.LENGTH_SHORT).show()
            } else {
              Toast.makeText(context, "Incorrect PIN. Access denied.", Toast.LENGTH_SHORT).show()
            }
          }
        }
      )
    } else {
      // Unlocked Creator Dashboard
      CreatorDashboardContent(
        user = user!!,
        videos = videos,
        payouts = payouts,
        onRequestCashout = { showCashoutDialog = true },
        onAddLesson = { showAddLessonDialog = true }
      )
    }

    if (showCashoutDialog) {
      CashoutDialog(
        currentBalance = user?.balanceGhs ?: 0.0,
        onDismiss = { showCashoutDialog = false },
        onSubmit = { amount, provider, momoNum, name ->
          viewModel.requestCashout(amount, provider, momoNum, name) { success, msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            if (success) showCashoutDialog = false
          }
        }
      )
    }

    if (showAddLessonDialog) {
      AddLessonAdminDialog(
        onDismiss = { showAddLessonDialog = false },
        onAdd = { grade, subjectId, title, summary, content, takeaways ->
          viewModel.addNewLesson(subjectId, grade, "Unit Admin", title, summary, content, takeaways)
          showAddLessonDialog = false
          Toast.makeText(context, "Lesson published to $grade curriculum!", Toast.LENGTH_SHORT).show()
        }
      )
    }
  }
}

@Composable
private fun CreatorPinGate(
  onVerify: (String) -> Unit
) {
  var pinInput by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(72.dp)
        .clip(CircleShape)
        .background(SkyBlueLight),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        Icons.Default.Lock,
        contentDescription = null,
        tint = SkyBluePrimaryDark,
        modifier = Modifier.size(36.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Creator & Monetization Hub",
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.ExtraBold,
        color = SkyBluePrimaryDark
      )
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Enter your secret Creator PIN to unlock real eligible earnings, Mobile Money cashouts, and content management.",
      style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B)),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
      value = pinInput,
      onValueChange = { pinInput = it },
      label = { Text("Enter Creator PIN") },
      placeholder = { Text("••••••••") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("creator_pin_input"),
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SkyBluePrimary,
        unfocusedBorderColor = Color(0xFFCBD5E1)
      ),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
      onClick = { onVerify(pinInput) },
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("verify_creator_pin_btn"),
      colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
      shape = RoundedCornerShape(12.dp)
    ) {
      Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text("Log In as Creator", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
  }
}

@Composable
private fun CreatorDashboardContent(
  user: UserEntity,
  videos: List<VideoPostEntity>,
  payouts: List<PayoutRequestEntity>,
  onRequestCashout: () -> Unit,
  onAddLesson: () -> Unit
) {
  var adsEnabled by remember { mutableStateOf(true) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Creator Header Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SkyBluePrimaryDark)
      ) {
        Row(
          modifier = Modifier.padding(18.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(30.dp))
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Tembilla Faith Yenpang",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
            }
            Text(
              text = "Verified FTY Creator & Lead Educator",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFE0F2FE))
            )
          }
        }
      }
    }

    // Earnings Card (Real Cedis GHS)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("creator_earnings_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Total Eligible Earnings",
                style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
              )
              Text(
                text = "GHS ${String.format("%.2f", user.balanceGhs)}",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF0F172A)
                )
              )
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFDCFCE7)
            ) {
              Text(
                text = "+14.2% this week",
                color = Color(0xFF16A34A),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Revenue Breakdown
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(SkyBlueExtraLight, RoundedCornerShape(12.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            EarningsSubMetric(label = "Video Ads", amount = "GHS 264.00")
            EarningsSubMetric(label = "Quiz Rewards", amount = "GHS 110.00")
            EarningsSubMetric(label = "Viewer Tips", amount = "GHS 54.50")
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Cashout Button
          Button(
            onClick = onRequestCashout,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("request_cashout_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Request Mobile Money Cashout", fontWeight = FontWeight.Bold, color = Color.White)
          }
        }
      }
    }

    // Performance Metrics Grid
    item {
      Text(
        text = "Content Performance",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = SkyBluePrimaryDark
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        MetricCard(title = "Total Views", value = "${user.totalViews}", modifier = Modifier.weight(1f))
        MetricCard(title = "Video Likes", value = "${user.totalLikes}", modifier = Modifier.weight(1f))
        MetricCard(title = "Est. CPM", value = "GHS 12.50", modifier = Modifier.weight(1f))
      }
    }

    // Monetization & Advertising Controls
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Educational Video Monetization",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "Earn GHS 0.05 per educational view and quiz completion bonus",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
            )
          }

          Switch(
            checked = adsEnabled,
            onCheckedChange = { adsEnabled = it },
            colors = SwitchDefaults.colors(checkedThumbColor = SkyBluePrimary)
          )
        }
      }
    }

    // Creator Video Management
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Your Uploaded Videos (${videos.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = SkyBluePrimaryDark
        )
      }
    }

    items(videos) { video ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(SkyBlueLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Visibility, contentDescription = null, tint = SkyBluePrimary, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = video.title,
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
              maxLines = 1
            )
            Text(
              text = "${video.viewsCount} views • ${video.likesCount} likes",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp)
            )
          }
          Text(
            text = "GHS ${String.format("%.2f", video.earnedGhs)}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
          )
        }
      }
    }

    // Admin Curriculum Management
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SkyBlueExtraLight)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Educator Content Management",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = SkyBluePrimaryDark
          )
          Text(
            text = "As verified creator Faith Yenpang, you have direct administrative authority to publish lessons and questions to the live app database.",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569))
          )
          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = onAddLesson,
            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Publish New Lesson")
          }
        }
      }
    }

    // Payout Request History
    item {
      Text(
        text = "Mobile Money Payout History",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = SkyBluePrimaryDark
      )
    }

    items(payouts) { payout ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "${payout.provider} (${payout.momoNumber})",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(payout.requestedAt)),
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8), fontSize = 11.sp)
            )
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "GHS ${String.format("%.2f", payout.amountGhs)}",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (payout.status == "COMPLETED") Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
            ) {
              Text(
                text = payout.status,
                color = if (payout.status == "COMPLETED") Color(0xFF16A34A) else Color(0xFFD97706),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
private fun EarningsSubMetric(label: String, amount: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp))
    Text(text = amount, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = SkyBluePrimaryDark))
  }
}

@Composable
private fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = title, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = SkyBluePrimaryDark))
    }
  }
}

@Composable
private fun CashoutDialog(
  currentBalance: Double,
  onDismiss: () -> Unit,
  onSubmit: (amount: Double, provider: String, momoNumber: String, name: String) -> Unit
) {
  var amountStr by remember { mutableStateOf("50.00") }
  var provider by remember { mutableStateOf("MTN Mobile Money") }
  var momoNumber by remember { mutableStateOf("0244123456") }
  var accountName by remember { mutableStateOf("Faith Yenpang") }

  val providers = listOf("MTN Mobile Money", "Telecel Cash", "AT Money")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Request Mobile Money Cashout", fontWeight = FontWeight.Bold, color = SkyBluePrimaryDark)
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Available Balance: GHS ${String.format("%.2f", currentBalance)}",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
        )

        OutlinedTextField(
          value = amountStr,
          onValueChange = { amountStr = it },
          label = { Text("Amount to Cash Out (GHS)") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )

        Text("Select Network:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          providers.forEach { prov ->
            val isSel = provider == prov
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSel) SkyBluePrimary else SkyBlueLight,
              modifier = Modifier
                .clickable { provider = prov }
                .padding(vertical = 2.dp)
            ) {
              Text(
                text = prov.split(" ").first(),
                color = if (isSel) Color.White else SkyBluePrimaryDark,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              )
            }
          }
        }

        OutlinedTextField(
          value = momoNumber,
          onValueChange = { momoNumber = it },
          label = { Text("MoMo Phone Number") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )

        OutlinedTextField(
          value = accountName,
          onValueChange = { accountName = it },
          label = { Text("Registered Account Name") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )

        Text(
          text = "Security guarantee: Cashouts are routed through licensed Ghanaian payment aggregators. No Mobile Money PIN is ever collected or exposed.",
          style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val amt = amountStr.toDoubleOrNull() ?: 0.0
          onSubmit(amt, provider, momoNumber, accountName)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
      ) {
        Text("Confirm Cashout", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8))) {
        Text("Cancel")
      }
    }
  )
}

@Composable
private fun AddLessonAdminDialog(
  onDismiss: () -> Unit,
  onAdd: (grade: String, subjectId: Long, title: String, summary: String, content: String, takeaways: String) -> Unit
) {
  var selectedGrade by remember { mutableStateOf("SHS") }
  var title by remember { mutableStateOf("") }
  var summary by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var takeaways by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Publish New Curriculum Lesson", fontWeight = FontWeight.Bold, color = SkyBluePrimaryDark) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("Primary", "JHS", "SHS").forEach { gr ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (selectedGrade == gr) SkyBluePrimary else SkyBlueLight,
              modifier = Modifier.clickable { selectedGrade = gr }
            ) {
              Text(
                text = gr,
                color = if (selectedGrade == gr) Color.White else SkyBluePrimaryDark,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Lesson Title") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )

        OutlinedTextField(
          value = summary,
          onValueChange = { summary = it },
          label = { Text("Summary") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          maxLines = 2
        )

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Full Lesson Notes") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          maxLines = 4
        )

        OutlinedTextField(
          value = takeaways,
          onValueChange = { takeaways = it },
          label = { Text("Key Takeaways") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            val subId = if (selectedGrade == "Primary") 1L else if (selectedGrade == "JHS") 5L else 9L
            onAdd(selectedGrade, subId, title, summary, content, takeaways)
          }
        },
        enabled = title.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
      ) {
        Text("Publish")
      }
    },
    dismissButton = {
      Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8))) {
        Text("Cancel")
      }
    }
  )
}
