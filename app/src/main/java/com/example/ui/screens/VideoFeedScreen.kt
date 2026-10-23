package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.VideoPostEntity
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFeedScreen(
  viewModel: FtyViewModel,
  modifier: Modifier = Modifier
) {
  val videos by viewModel.allVideos.collectAsStateWithLifecycle()
  val activeVideo by viewModel.activeCommentVideo.collectAsStateWithLifecycle()
  val context = LocalContext.current

  var showUploadDialog by remember { mutableStateOf(false) }
  var showReportDialog by remember { mutableStateOf(false) }
  var reportedVideoTitle by remember { mutableStateOf("") }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("video_feed_screen")
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header Section
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "FTY Short Videos",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = SkyBluePrimaryDark
              )
            )
            Text(
              text = "Educational bytes, tips & student motivation",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
            )
          }

          Button(
            onClick = { showUploadDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("upload_video_header_btn")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Upload", fontWeight = FontWeight.Bold)
          }
        }
      }

      // Video Feed List
      items(videos, key = { it.id }) { video ->
        VideoPlayerCard(
          video = video,
          onLike = { viewModel.toggleLikeVideo(video) },
          onCommentClick = { viewModel.openComments(video) },
          onReportClick = {
            reportedVideoTitle = video.title
            showReportDialog = true
          },
          onShareClick = {
            Toast.makeText(context, "Sharing link to \"${video.title}\"", Toast.LENGTH_SHORT).show()
          }
        )
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }

    // Comments Sheet
    if (activeVideo != null) {
      CommentsSheet(
        video = activeVideo!!,
        onDismiss = { viewModel.closeComments() },
        onSubmitComment = { text -> viewModel.submitComment(activeVideo!!.id, text) }
      )
    }

    // Upload Video Dialog
    if (showUploadDialog) {
      UploadVideoDialog(
        onDismiss = { showUploadDialog = false },
        onUpload = { title, desc, category ->
          viewModel.uploadVideo(title, desc, category, 45)
          showUploadDialog = false
          Toast.makeText(context, "Video uploaded & monetized successfully!", Toast.LENGTH_LONG).show()
        }
      )
    }

    // Report Video Dialog
    if (showReportDialog) {
      AlertDialog(
        onDismissRequest = { showReportDialog = false },
        title = { Text("Report Video", fontWeight = FontWeight.Bold) },
        text = {
          Text("Are you reporting \"$reportedVideoTitle\" for inappropriate content? FTY moderation will review it within 2 hours.")
        },
        confirmButton = {
          Button(
            onClick = {
              showReportDialog = false
              Toast.makeText(context, "Thank you. Video flagged for community review.", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
          ) {
            Text("Confirm Report")
          }
        },
        dismissButton = {
          Button(
            onClick = { showReportDialog = false },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8))
          ) {
            Text("Cancel")
          }
        }
      )
    }
  }
}

@Composable
private fun VideoPlayerCard(
  video: VideoPostEntity,
  onLike: () -> Unit,
  onCommentClick: () -> Unit,
  onReportClick: () -> Unit,
  onShareClick: () -> Unit
) {
  var isPlaying by remember { mutableStateOf(true) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("video_card_${video.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Column {
      // Simulated Video Player Canvas
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp)
          .background(Color(0xFF0F172A))
          .clickable { isPlaying = !isPlaying }
      ) {
        // Background gradient mimicking rich video
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  SkyBluePrimaryDark.copy(alpha = 0.5f),
                  Color(0xFF0F172A)
                )
              )
            )
        )

        // Center Play / Pause indicator
        Box(
          modifier = Modifier
            .align(Alignment.Center)
            .size(54.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = if (isPlaying) 0.15f else 0.85f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            if (isPlaying) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
            contentDescription = null,
            tint = if (isPlaying) Color.White else SkyBluePrimaryDark,
            modifier = Modifier.size(32.dp)
          )
        }

        // Top Category & Monetization Badge
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .align(Alignment.TopStart),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.6f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Videocam, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = video.category,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
          }

          if (video.isMonetized) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFF59E0B).copy(alpha = 0.9f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text("Monetized", color = Color.White, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold))
              }
            }
          }
        }

        // Bottom Progress Bar
        LinearProgressIndicator(
          progress = { if (isPlaying) 0.65f else 0.3f },
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .align(Alignment.BottomCenter),
          color = SkyBluePrimary,
          trackColor = Color.White.copy(alpha = 0.2f)
        )
      }

      // Creator & Video Details
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(SkyBlueLight),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = video.creatorName.take(1),
                color = SkyBluePrimaryDark,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = video.creatorName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0F172A)
              )
              Text(
                text = "${video.viewsCount} views • ~${video.durationSec}s",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp)
              )
            }
          }

          IconButton(onClick = onReportClick) {
            Icon(Icons.Default.Flag, contentDescription = "Report", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = video.title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = video.description,
          style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons Row (Like, Comment, Share)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Like Button
          Row(
            modifier = Modifier
              .clickable(onClick = onLike)
              .padding(8.dp)
              .testTag("video_like_btn_${video.id}"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              if (video.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (video.isLikedByMe) Color(0xFFEF4444) else Color(0xFF64748B),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${video.likesCount}",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = if (video.isLikedByMe) Color(0xFFEF4444) else Color(0xFF334155)
              )
            )
          }

          // Comment Button
          Row(
            modifier = Modifier
              .clickable(onClick = onCommentClick)
              .padding(8.dp)
              .testTag("video_comment_btn_${video.id}"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment", tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${video.commentsCount}",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
            )
          }

          // Share Button
          Row(
            modifier = Modifier
              .clickable(onClick = onShareClick)
              .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Share", style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF334155)))
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentsSheet(
  video: VideoPostEntity,
  onDismiss: () -> Unit,
  onSubmitComment: (String) -> Unit
) {
  var commentInput by remember { mutableStateOf("") }
  val sampleComments = listOf(
    "Faith Yenpang: Education is power! Keep learning everyone.",
    "Very helpful math short, understood in under a minute!",
    "FTY Fast Data is super fast, watched this with no buffering!"
  )

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = Color.White
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("comments_sheet")
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Comments (${sampleComments.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = SkyBluePrimaryDark
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(sampleComments) { commentText ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = SkyBlueExtraLight)
          ) {
            Text(
              text = commentText,
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF1E293B)),
              modifier = Modifier.padding(10.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = commentInput,
          onValueChange = { commentInput = it },
          placeholder = { Text("Write a positive comment...") },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SkyBluePrimary,
            unfocusedBorderColor = Color(0xFFE2E8F0)
          ),
          singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = {
            if (commentInput.isNotBlank()) {
              onSubmitComment(commentInput)
              commentInput = ""
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Send, contentDescription = "Send")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun UploadVideoDialog(
  onDismiss: () -> Unit,
  onUpload: (title: String, desc: String, category: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("Education") }
  val categories = listOf("Education", "Science", "Mathematics", "Motivation", "Community")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Upload Educational Short",
        fontWeight = FontWeight.Bold,
        color = SkyBluePrimaryDark
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Share learning tips, solutions, or insights with thousands of Ghanaian students. (Max 50MB, MP4 format).",
          style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
        )

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Video Title") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Brief Description & Tags") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          maxLines = 3
        )

        Text("Category:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          categories.take(3).forEach { cat ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (selectedCategory == cat) SkyBluePrimary else SkyBlueLight,
              modifier = Modifier
                .clickable { selectedCategory = cat }
                .padding(vertical = 2.dp)
            ) {
              Text(
                text = cat,
                color = if (selectedCategory == cat) Color.White else SkyBluePrimaryDark,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onUpload(title, description, selectedCategory)
          }
        },
        enabled = title.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
      ) {
        Text("Publish & Monetize")
      }
    },
    dismissButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8))
      ) {
        Text("Cancel")
      }
    }
  )
}
