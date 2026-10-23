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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel

@Composable
fun ProfileScreen(
  viewModel: FtyViewModel,
  onOpenCreatorHub: () -> Unit,
  modifier: Modifier = Modifier
) {
  val user by viewModel.currentUser.collectAsStateWithLifecycle()
  val context = LocalContext.current

  var showEditProfileDialog by remember { mutableStateOf(false) }
  var allowDirectMessages by remember { mutableStateOf(true) }
  var publicStudyStats by remember { mutableStateOf(true) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("profile_screen")
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Profile Header Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(SkyBlueLight),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = (user?.displayName ?: "Faith").take(1),
                style = MaterialTheme.typography.headlineLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = SkyBluePrimaryDark
                )
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = user?.displayName ?: "Faith Yenpang",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )
              )
              if (user?.isCreator == true) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  Icons.Default.CheckCircle,
                  contentDescription = "Verified Creator",
                  tint = Color(0xFF10B981),
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            Text(
              text = "@${user?.username ?: "tembilla_learner"} • ${user?.gradeLevel ?: "SHS"} Level",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = user?.bio ?: "Exploring science, math, and code on FTY.",
              style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF334155)),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = { showEditProfileDialog = true },
              modifier = Modifier.testTag("edit_profile_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = SkyBlueLight),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.Edit, contentDescription = null, tint = SkyBluePrimaryDark, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Edit Profile", color = SkyBluePrimaryDark, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }

      // Learning Progress & Badges Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Academic Milestones & Badges",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = SkyBluePrimaryDark
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              BadgeItem(name = "Math Whiz", desc = "10+ Quizzes", icon = Icons.Default.EmojiEvents, color = Color(0xFFF59E0B))
              BadgeItem(name = "Science Star", desc = "Advanced Units", icon = Icons.Default.Star, color = SkyBluePrimary)
              BadgeItem(name = "Community", desc = "Active Peer", icon = Icons.Default.Badge, color = Color(0xFF10B981))
            }
          }
        }
      }

      // Creator Mode Quick Access
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenCreatorHub)
            .testTag("profile_creator_hub_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = if (user?.isCreator == true) Color(0xFFDCFCE7) else SkyBlueExtraLight)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (user?.isCreator == true) Color(0xFF10B981) else SkyBluePrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                if (user?.isCreator == true) Icons.Default.Star else Icons.Default.Key,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (user?.isCreator == true) "Creator Studio & MoMo Payouts" else "Creator & Educator Portal",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0F172A)
              )
              Text(
                text = if (user?.isCreator == true) "Manage video earnings (GHS ${String.format("%.2f", user?.balanceGhs ?: 0.0)})" else "Access creator earnings and publishing portal",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
              )
            }
          }
        }
      }

      // Privacy Settings
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Privacy & Notifications",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = SkyBluePrimaryDark
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Allow Direct Mentorship Messages", style = MaterialTheme.typography.bodyMedium)
              Switch(
                checked = allowDirectMessages,
                onCheckedChange = { allowDirectMessages = it },
                colors = SwitchDefaults.colors(checkedThumbColor = SkyBluePrimary)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Display Quiz Scores on Leaderboard", style = MaterialTheme.typography.bodyMedium)
              Switch(
                checked = publicStudyStats,
                onCheckedChange = { publicStudyStats = it },
                colors = SwitchDefaults.colors(checkedThumbColor = SkyBluePrimary)
              )
            }
          }
        }
      }

      // Logout / Switch Account
      item {
        Button(
          onClick = {
            Toast.makeText(context, "Signed out safely. Account session secured.", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.12f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFDC2626))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Switch Account / Log Out", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }

    if (showEditProfileDialog) {
      EditProfileDialog(
        currentName = user?.displayName ?: "",
        currentBio = user?.bio ?: "",
        currentGrade = user?.gradeLevel ?: "SHS",
        onDismiss = { showEditProfileDialog = false },
        onSave = { name, bio, grade ->
          viewModel.updateProfile(name, bio, grade)
          showEditProfileDialog = false
          Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        }
      )
    }
  }
}

@Composable
private fun BadgeItem(name: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
        .background(color.copy(alpha = 0.15f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(text = name, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
    Text(text = desc, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
  }
}

@Composable
private fun EditProfileDialog(
  currentName: String,
  currentBio: String,
  currentGrade: String,
  onDismiss: () -> Unit,
  onSave: (String, String, String) -> Unit
) {
  var name by remember { mutableStateOf(currentName) }
  var bio by remember { mutableStateOf(currentBio) }
  var grade by remember { mutableStateOf(currentGrade) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Edit Student Profile", fontWeight = FontWeight.Bold, color = SkyBluePrimaryDark) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Full Name") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )
        OutlinedTextField(
          value = bio,
          onValueChange = { bio = it },
          label = { Text("Bio / Academic Interests") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          maxLines = 2
        )
        Text("Grade Level:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf("Primary", "JHS", "SHS").forEach { gr ->
            val sel = grade == gr
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (sel) SkyBluePrimary else SkyBlueLight,
              modifier = Modifier.clickable { grade = gr }
            ) {
              Text(
                text = gr,
                color = if (sel) Color.White else SkyBluePrimaryDark,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { if (name.isNotBlank()) onSave(name, bio, grade) },
        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
      ) {
        Text("Save")
      }
    },
    dismissButton = {
      Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8))) {
        Text("Cancel")
      }
    }
  )
}
