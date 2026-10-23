package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.navigation.Screen
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel

@Composable
fun HomeScreen(
  viewModel: FtyViewModel,
  onNavigate: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val user by viewModel.currentUser.collectAsStateWithLifecycle()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("home_screen"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // Top Welcome Header
    item {
      HeaderSection(
        displayName = user?.displayName ?: "Student",
        onProfileClick = { onNavigate(Screen.Profile.route) }
      )
    }

    // Hero Banner Card
    item {
      HeroBannerSection(
        onExploreClick = { onNavigate(Screen.Education.route) },
        onBuyDataClick = { onNavigate(Screen.DataBundle.route) }
      )
    }

    // Fast Data Highlight Banner
    item {
      FastDataHighlightCard(onBuyDataClick = { onNavigate(Screen.DataBundle.route) })
    }

    // Feature Shortcuts Grid
    item {
      Text(
        text = "Feature Shortcuts",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = SkyBluePrimaryDark,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
      )

      ShortcutGrid(onNavigate = onNavigate)
    }

    // User Progress & Badges Card
    item {
      LearningProgressSummary(
        points = user?.points ?: 0,
        quizzesPassed = user?.quizzesPassed ?: 0,
        balanceGhs = user?.balanceGhs ?: 0.0,
        isCreator = user?.isCreator == true,
        onCreatorClick = { onNavigate(Screen.Creator.route) }
      )
    }

    // Inspirational Quote from Tembilla Faith Yenpang
    item {
      FounderQuoteCard()
    }
  }
}

@Composable
private fun HeaderSection(
  displayName: String,
  onProfileClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(horizontal = 16.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(SkyBlueLight),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.fty_logo),
          contentDescription = "FTY Emblem",
          modifier = Modifier.size(36.dp),
          contentScale = ContentScale.Crop
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "FTY App",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            color = SkyBluePrimary
          )
        )
        Text(
          text = "Tembilla Faith Yenpang",
          style = MaterialTheme.typography.bodySmall.copy(
            color = Color(0xFF64748B),
            fontWeight = FontWeight.Medium
          )
        )
      }
    }

    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SkyBlueLight,
      modifier = Modifier
        .clickable(onClick = onProfileClick)
        .testTag("header_profile_badge")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color(0xFF10B981))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = displayName.split(" ").firstOrNull() ?: "Faith",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = SkyBluePrimaryDark
          )
        )
      }
    }
  }
}

@Composable
private fun HeroBannerSection(
  onExploreClick: () -> Unit,
  onBuyDataClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("hero_banner_card"),
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.fty_hero_banner),
          contentDescription = "FTY Digital Academy Banner",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
        // Soft gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
              )
            )
        )
        Text(
          text = "Learn • Communicate • Grow",
          color = Color.White,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(14.dp)
        )
      }

      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Welcome to FTY Academy",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = SkyBluePrimaryDark
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Empowering Ghanaian students and youth with interactive Primary, JHS, and SHS lessons, study groups, educational videos, and affordable internet data.",
          style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF475569)),
          lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onExploreClick,
            modifier = Modifier
              .weight(1f)
              .testTag("hero_start_learning_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Start Learning", fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = onBuyDataClick,
            modifier = Modifier
              .weight(1f)
              .testTag("hero_fast_data_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7).copy(alpha = 0.15f)),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
              Icons.Default.ElectricBolt,
              contentDescription = null,
              tint = SkyBluePrimaryDark,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Fast Data", color = SkyBluePrimaryDark, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
private fun FastDataHighlightCard(onBuyDataClick: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clickable(onClick = onBuyDataClick)
      .testTag("fast_data_highlight_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF0369A1))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            Icons.Default.ElectricBolt,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(26.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "FTY Fast Data Bundles",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
          Text(
            text = "MTN, Telecel & AT Ghana • Affordable student data",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFE0F2FE))
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF59E0B),
        modifier = Modifier.padding(start = 8.dp)
      ) {
        Text(
          text = "BUY NOW",
          color = Color.White,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }
    }
  }
}

@Composable
private fun ShortcutGrid(onNavigate: (String) -> Unit) {
  val shortcuts = listOf(
    ShortcutItem("Learn", "Primary, JHS, SHS", Icons.Default.School, SkyBluePrimary, Screen.Education.route),
    ShortcutItem("Fast Data", "DataPlug Portal", Icons.Default.LocalOffer, Color(0xFF0284C7), Screen.DataBundle.route),
    ShortcutItem("Videos", "Short Clips & Tips", Icons.Default.VideoLibrary, Color(0xFF0EA5E9), Screen.Videos.route),
    ShortcutItem("Chat", "Study Groups", Icons.Default.Forum, Color(0xFF0369A1), Screen.Chat.route),
    ShortcutItem("Calculator", "Scientific Tool", Icons.Default.Calculate, Color(0xFF0284C7), Screen.Calculator.route),
    ShortcutItem("Creator Hub", "Earnings & PIN", Icons.Default.MonetizationOn, Color(0xFFD97706), Screen.Creator.route)
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    for (i in shortcuts.indices step 2) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        ShortcutCard(
          item = shortcuts[i],
          modifier = Modifier.weight(1f),
          onClick = { onNavigate(shortcuts[i].route) }
        )
        if (i + 1 < shortcuts.size) {
          ShortcutCard(
            item = shortcuts[i + 1],
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(shortcuts[i + 1].route) }
          )
        }
      }
    }
  }
}

private data class ShortcutItem(
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val color: Color,
  val route: String
)

@Composable
private fun ShortcutCard(
  item: ShortcutItem,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag("shortcut_${item.title.lowercase().replace(" ", "_")}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(item.color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          item.icon,
          contentDescription = null,
          tint = item.color,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = item.title,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF0F172A)
        )
        Text(
          text = item.subtitle,
          style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp),
          maxLines = 1
        )
      }
    }
  }
}

@Composable
private fun LearningProgressSummary(
  points: Int,
  quizzesPassed: Int,
  balanceGhs: Double,
  isCreator: Boolean,
  onCreatorClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("progress_summary_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SkyBlueExtraLight)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Your Platform Standing",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = SkyBluePrimaryDark
        )
        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFF59E0B))
      }
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        StatPill(label = "Study XP", value = "$points pts")
        StatPill(label = "Quizzes Passed", value = "$quizzesPassed")
        StatPill(
          label = if (isCreator) "Creator Balance" else "Creator PIN",
          value = if (isCreator) "GHS ${String.format("%.2f", balanceGhs)}" else "Unlock"
        )
      }

      if (!isCreator) {
        Spacer(modifier = Modifier.height(10.dp))
        Button(
          onClick = onCreatorClick,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("creator_unlock_home_btn"),
          colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Creator & Educator Login", fontWeight = FontWeight.SemiBold)
        }
      }
    }
  }
}

@Composable
private fun StatPill(label: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.ExtraBold,
        color = SkyBluePrimaryDark
      )
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp)
    )
  }
}

@Composable
private fun FounderQuoteCard() {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "\"When we equip one student with digital tools, lessons, and affordable data, we ignite the potential of an entire community.\"",
        style = MaterialTheme.typography.bodyMedium.copy(
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          color = Color(0xFF334155),
          lineHeight = 22.sp
        )
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "— Tembilla Faith Yenpang, Founder",
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          color = SkyBluePrimary
        )
      )
    }
  }
}
