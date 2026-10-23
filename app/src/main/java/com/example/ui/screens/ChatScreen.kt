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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.ChatGroupEntity
import com.example.data.model.ChatMessageEntity
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
  viewModel: FtyViewModel,
  modifier: Modifier = Modifier
) {
  val chatGroups by viewModel.chatGroups.collectAsStateWithLifecycle()
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
  val context = LocalContext.current

  var selectedTab by remember { mutableIntStateOf(0) } // 0: Public Groups, 1: Private Messages
  var activeGroup by remember { mutableStateOf<ChatGroupEntity?>(null) }
  var isPrivateRoomOpen by remember { mutableStateOf(false) }
  var showCreateGroupDialog by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("chat_screen")
  ) {
    when {
      activeGroup != null -> {
        ChatRoomView(
          title = activeGroup!!.name,
          subtitle = "${activeGroup!!.memberCount} members • ${activeGroup!!.gradeLevel}",
          isPrivate = false,
          currentUserId = currentUser?.id ?: 1,
          onBack = { activeGroup = null },
          onSendMessage = { text -> viewModel.sendGroupMessage(activeGroup!!.id, text) },
          onReport = { msgId -> viewModel.flagMessage(msgId) }
        )
      }
      isPrivateRoomOpen -> {
        ChatRoomView(
          title = "Faith Yenpang (Mentor)",
          subtitle = "Private Mentorship & Support",
          isPrivate = true,
          currentUserId = currentUser?.id ?: 1,
          onBack = { isPrivateRoomOpen = false },
          onSendMessage = { text -> viewModel.sendPrivateMessage(99, text) },
          onReport = { msgId -> viewModel.flagMessage(msgId) }
        )
      }
      else -> {
        Column(modifier = Modifier.fillMaxSize()) {
          // Top Header & Create Action
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.White)
              .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "FTY Community Chat",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = SkyBluePrimaryDark
                )
              )
              Text(
                text = "Learn together, ask questions & share insights",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
              )
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = SkyBlueLight,
              modifier = Modifier.clickable { showCreateGroupDialog = true }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = SkyBluePrimaryDark, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "New Group",
                  color = SkyBluePrimaryDark,
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }

          // Tabs: Public Groups vs Private Mentorship
          TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = SkyBluePrimary
          ) {
            Tab(
              selected = selectedTab == 0,
              onClick = { selectedTab = 0 },
              text = { Text("Public Study Groups", fontWeight = FontWeight.Bold) },
              icon = { Icon(Icons.Default.Group, contentDescription = null) }
            )
            Tab(
              selected = selectedTab == 1,
              onClick = { selectedTab = 1 },
              text = { Text("Private Mentorship", fontWeight = FontWeight.Bold) },
              icon = { Icon(Icons.Default.Lock, contentDescription = null) }
            )
          }

          // Content List
          if (selectedTab == 0) {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              contentPadding = PaddingValues(16.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              items(chatGroups) { group ->
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { activeGroup = group }
                    .testTag("group_item_${group.id}"),
                  shape = RoundedCornerShape(14.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Box(
                      modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(SkyBlueLight),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(Icons.Default.Forum, contentDescription = null, tint = SkyBluePrimary, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                      ) {
                        Text(
                          text = group.name,
                          style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                          color = Color(0xFF0F172A)
                        )
                        Surface(
                          shape = RoundedCornerShape(6.dp),
                          color = SkyBlueExtraLight
                        ) {
                          Text(
                            text = group.gradeLevel,
                            color = SkyBluePrimaryDark,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                          )
                        }
                      }
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(
                        text = group.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)),
                        maxLines = 1
                      )
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(
                        text = "${group.memberCount} active members",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF10B981), fontWeight = FontWeight.SemiBold)
                      )
                    }
                  }
                }
              }
              item { Spacer(modifier = Modifier.height(80.dp)) }
            }
          } else {
            // Private Mentorship Tab
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              contentPadding = PaddingValues(16.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              item {
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isPrivateRoomOpen = true }
                    .testTag("private_mentor_card"),
                  shape = RoundedCornerShape(14.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Box(
                      modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2FE)),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(Icons.Default.Person, contentDescription = null, tint = SkyBluePrimaryDark, modifier = Modifier.size(26.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                      Text(
                        text = "Faith Yenpang (Creator & Mentor)",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF0F172A)
                      )
                      Text(
                        text = "Private Academic Guidance & Questions",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                      )
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(
                        text = "End-to-end encrypted private session",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0369A1), fontWeight = FontWeight.Medium)
                      )
                    }
                  }
                }
              }
              item { Spacer(modifier = Modifier.height(80.dp)) }
            }
          }
        }
      }
    }

    // Create Group Dialog
    if (showCreateGroupDialog) {
      CreateGroupDialog(
        onDismiss = { showCreateGroupDialog = false },
        onCreate = { name, desc, grade ->
          viewModel.createNewGroup(name, desc, grade)
          showCreateGroupDialog = false
          Toast.makeText(context, "Study group created!", Toast.LENGTH_SHORT).show()
        }
      )
    }
  }
}

@Composable
private fun ChatRoomView(
  title: String,
  subtitle: String,
  isPrivate: Boolean,
  currentUserId: Long,
  onBack: () -> Unit,
  onSendMessage: (String) -> Unit,
  onReport: (Long) -> Unit
) {
  var messageInput by remember { mutableStateOf("") }
  val context = LocalContext.current

  // Sample lively messages for the study room
  val sampleMessages = remember {
    mutableListOf(
      ChatMessageEntity(
        id = 101,
        groupId = 1,
        senderId = 99,
        senderName = "Faith Yenpang (Mentor)",
        senderAvatar = "",
        text = "Welcome everyone! Feel free to ask any question regarding today's lesson.",
        timestamp = System.currentTimeMillis() - 7200000,
        isPrivate = isPrivate
      ),
      ChatMessageEntity(
        id = 102,
        groupId = 1,
        senderId = 2,
        senderName = "Kojo Darko",
        senderAvatar = "",
        text = "Hello Mentor, how do we quickly memorize the quadratic formula?",
        timestamp = System.currentTimeMillis() - 3600000,
        isPrivate = isPrivate
      ),
      ChatMessageEntity(
        id = 103,
        groupId = 1,
        senderId = 99,
        senderName = "Faith Yenpang (Mentor)",
        senderAvatar = "",
        text = "Remember the rhyme: 'A negative boy (-b) couldn't decide (±) whether to go to a radical party (√) because he was square (b²) and lost 4 awesome chicks (-4ac) all over 2 apples (2a)!'",
        timestamp = System.currentTimeMillis() - 1800000,
        isPrivate = isPrivate
      )
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
  ) {
    // Room Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(horizontal = 8.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SkyBluePrimaryDark)
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF0F172A)
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp)
        )
      }
      IconButton(onClick = {
        Toast.makeText(context, "Community safety options: All messages monitored against abuse.", Toast.LENGTH_SHORT).show()
      }) {
        Icon(Icons.Default.Security, contentDescription = "Safety", tint = SkyBluePrimary)
      }
    }

    // Message History
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(sampleMessages) { msg ->
        val isMe = msg.senderId == currentUserId
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeStr = dateFormat.format(Date(msg.timestamp))

        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
          if (!isMe) {
            Text(
              text = msg.senderName,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SkyBluePrimaryDark),
              modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            )
          }

          Surface(
            shape = RoundedCornerShape(
              topStart = 16.dp,
              topEnd = 16.dp,
              bottomStart = if (isMe) 16.dp else 4.dp,
              bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            color = if (isMe) SkyBluePrimary else Color.White,
            shadowElevation = 1.dp
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = msg.text,
                color = if (isMe) Color.White else Color(0xFF0F172A),
                style = MaterialTheme.typography.bodyMedium
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = timeStr,
                color = if (isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF94A3B8),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                modifier = Modifier.align(Alignment.End)
              )
            }
          }
        }
      }
    }

    // Input Box
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = messageInput,
        onValueChange = { messageInput = it },
        placeholder = { Text("Write your message...") },
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = SkyBluePrimary,
          unfocusedBorderColor = Color(0xFFE2E8F0)
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = {
          if (messageInput.isNotBlank()) {
            val newMsg = ChatMessageEntity(
              id = System.currentTimeMillis(),
              groupId = 1,
              senderId = currentUserId,
              senderName = "You",
              senderAvatar = "",
              text = messageInput.trim(),
              timestamp = System.currentTimeMillis(),
              isPrivate = isPrivate
            )
            sampleMessages.add(newMsg)
            onSendMessage(messageInput.trim())
            messageInput = ""
          }
        },
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(SkyBluePrimary)
      ) {
        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
      }
    }
  }
}

@Composable
private fun CreateGroupDialog(
  onDismiss: () -> Unit,
  onCreate: (name: String, desc: String, grade: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var gradeLevel by remember { mutableStateOf("SHS") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Create Study Group", fontWeight = FontWeight.Bold, color = SkyBluePrimaryDark) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Group Name") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Topic or Purpose") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          maxLines = 2
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { if (name.isNotBlank()) onCreate(name, description, gradeLevel) },
        enabled = name.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
      ) {
        Text("Create Group")
      }
    },
    dismissButton = {
      Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8))) {
        Text("Cancel")
      }
    }
  )
}
