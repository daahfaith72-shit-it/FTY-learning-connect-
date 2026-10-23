package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val username: String,
  val displayName: String,
  val email: String,
  val bio: String,
  val avatarUrl: String,
  val gradeLevel: String, // "Primary", "JHS", "SHS"
  val role: String, // "STUDENT", "CREATOR", "ADMIN"
  val isCreator: Boolean = false,
  val creatorPin: String = "faith1132A",
  val balanceGhs: Double = 345.50, // Real creator eligible earnings in GHS
  val pendingGhs: Double = 80.00,
  val totalViews: Long = 14250,
  val totalLikes: Long = 3120,
  val points: Int = 450,
  val quizzesPassed: Int = 12
)

@Entity(tableName = "subjects")
data class SubjectEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val gradeLevel: String, // "Primary", "JHS", "SHS"
  val title: String,
  val code: String,
  val iconName: String,
  val description: String,
  val lessonCount: Int = 4
)

@Entity(tableName = "lessons")
data class LessonEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val subjectId: Long,
  val gradeLevel: String,
  val classGrade: String, // "Class 4", "JHS 2", "SHS 1", etc.
  val title: String,
  val summary: String,
  val content: String,
  val keyTakeaways: String,
  val durationMin: Int,
  val difficulty: String, // "Beginner", "Standard", "Advanced"
  val isCompleted: Boolean = false
)

@Entity(tableName = "questions")
data class QuestionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val subjectId: Long,
  val lessonId: Long,
  val gradeLevel: String,
  val questionText: String,
  val optionA: String,
  val optionB: String,
  val optionC: String,
  val optionD: String,
  val correctOptionIndex: Int, // 0, 1, 2, 3
  val explanation: String,
  val isAdvanced: Boolean = false
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val subjectTitle: String,
  val gradeLevel: String,
  val score: Int,
  val totalQuestions: Int,
  val percentage: Int,
  val passed: Boolean,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_groups")
data class ChatGroupEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val description: String,
  val gradeLevel: String, // "Primary", "JHS", "SHS", "All"
  val memberCount: Int = 48,
  val iconName: String
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val groupId: Long?, // Null if private message
  val senderId: Long,
  val senderName: String,
  val senderAvatar: String,
  val recipientId: Long? = null, // Null if group message
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isPrivate: Boolean = false,
  val isFlagged: Boolean = false
)

@Entity(tableName = "video_posts")
data class VideoPostEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val creatorId: Long,
  val creatorName: String,
  val creatorAvatar: String,
  val title: String,
  val description: String,
  val category: String, // "Education", "Science", "Mathematics", "Inspiration", "Community"
  val durationSec: Int = 45,
  val likesCount: Int = 184,
  val commentsCount: Int = 32,
  val viewsCount: Int = 1240,
  val isLikedByMe: Boolean = false,
  val isMonetized: Boolean = true,
  val earnedGhs: Double = 12.40,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "video_comments")
data class VideoCommentEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val videoId: Long,
  val authorName: String,
  val authorAvatar: String,
  val text: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "payout_requests")
data class PayoutRequestEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val creatorId: Long,
  val amountGhs: Double,
  val provider: String, // "MTN Mobile Money", "Telecel Cash", "AT Money"
  val momoNumber: String,
  val accountName: String,
  val status: String, // "PENDING", "PROCESSING", "COMPLETED"
  val requestedAt: Long = System.currentTimeMillis()
)
