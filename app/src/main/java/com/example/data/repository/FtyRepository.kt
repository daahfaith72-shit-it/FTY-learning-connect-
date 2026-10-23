package com.example.data.repository

import com.example.data.local.AppDao
import com.example.data.local.DatabaseInitializer
import com.example.data.model.ChatGroupEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.LessonEntity
import com.example.data.model.PayoutRequestEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.QuizAttemptEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.UserEntity
import com.example.data.model.VideoCommentEntity
import com.example.data.model.VideoPostEntity
import kotlinx.coroutines.flow.Flow

class FtyRepository(private val dao: AppDao) {

  suspend fun initializeData() {
    DatabaseInitializer.populateIfEmpty(dao)
  }

  // User
  val currentUser: Flow<UserEntity?> = dao.getCurrentUserFlow()

  suspend fun verifyAndUnlockCreator(pin: String): Boolean {
    val trimmed = pin.trim()
    // Creator PIN requirement: faith1132A
    if (trimmed == "faith1132A") {
      val user = dao.getCurrentUser()
      if (user != null) {
        dao.unlockCreatorMode(user.id)
        return true
      }
    }
    return false
  }

  suspend fun updateUserProfile(displayName: String, bio: String, gradeLevel: String) {
    val user = dao.getCurrentUser() ?: return
    dao.updateUser(user.copy(displayName = displayName, bio = bio, gradeLevel = gradeLevel))
  }

  // Education: Subjects & Lessons
  fun getSubjectsByLevel(level: String): Flow<List<SubjectEntity>> = dao.getSubjectsByLevel(level)
  fun getAllSubjects(): Flow<List<SubjectEntity>> = dao.getAllSubjects()
  fun getLessonsBySubject(subjectId: Long): Flow<List<LessonEntity>> = dao.getLessonsBySubject(subjectId)
  fun getLessonsByLevel(level: String): Flow<List<LessonEntity>> = dao.getLessonsByLevel(level)
  fun searchLessons(query: String): Flow<List<LessonEntity>> = dao.searchLessons(query)

  suspend fun markLessonCompleted(lessonId: Long) {
    dao.markLessonCompleted(lessonId)
  }

  suspend fun addNewLesson(lesson: LessonEntity): Long {
    return dao.insertLesson(lesson)
  }

  suspend fun addNewQuestion(question: QuestionEntity): Long {
    return dao.insertQuestion(question)
  }

  // Quizzes
  suspend fun getQuestionsForSubject(subjectId: Long): List<QuestionEntity> = dao.getQuestionsBySubject(subjectId)
  suspend fun getQuestionsForLevel(level: String, advancedOnly: Boolean = false): List<QuestionEntity> {
    return if (advancedOnly) {
      dao.getAdvancedQuestionsByLevel(level)
    } else {
      dao.getQuestionsByLevel(level)
    }
  }

  val quizAttempts: Flow<List<QuizAttemptEntity>> = dao.getAllQuizAttempts()

  suspend fun recordQuizResult(subjectTitle: String, gradeLevel: String, score: Int, total: Int): QuizAttemptEntity {
    val percentage = if (total > 0) (score * 100) / total else 0
    val passed = percentage >= 60
    val attempt = QuizAttemptEntity(
      subjectTitle = subjectTitle,
      gradeLevel = gradeLevel,
      score = score,
      totalQuestions = total,
      percentage = percentage,
      passed = passed
    )
    dao.insertQuizAttempt(attempt)

    val user = dao.getCurrentUser()
    if (user != null && passed) {
      dao.recordQuizPassed(user.id, score * 10)
    }
    return attempt
  }

  // Chat
  val chatGroups: Flow<List<ChatGroupEntity>> = dao.getAllChatGroups()

  fun getGroupMessages(groupId: Long): Flow<List<ChatMessageEntity>> = dao.getGroupMessages(groupId)
  val privateMessages: Flow<List<ChatMessageEntity>> = dao.getPrivateMessages()

  suspend fun sendGroupMessage(groupId: Long, text: String) {
    val user = dao.getCurrentUser() ?: return
    val msg = ChatMessageEntity(
      groupId = groupId,
      senderId = user.id,
      senderName = if (user.isCreator) "${user.displayName} (Creator)" else user.displayName,
      senderAvatar = user.avatarUrl,
      text = text,
      isPrivate = false
    )
    dao.insertMessage(msg)
  }

  suspend fun sendPrivateMessage(recipientId: Long, text: String) {
    val user = dao.getCurrentUser() ?: return
    val msg = ChatMessageEntity(
      groupId = null,
      senderId = user.id,
      senderName = if (user.isCreator) "${user.displayName} (Creator)" else user.displayName,
      senderAvatar = user.avatarUrl,
      recipientId = recipientId,
      text = text,
      isPrivate = true
    )
    dao.insertMessage(msg)
  }

  suspend fun reportMessage(messageId: Long) {
    dao.flagMessage(messageId)
  }

  suspend fun createChatGroup(name: String, description: String, gradeLevel: String, iconName: String) {
    dao.insertChatGroup(
      ChatGroupEntity(
        name = name,
        description = description,
        gradeLevel = gradeLevel,
        memberCount = 1,
        iconName = iconName
      )
    )
  }

  // Videos
  val allVideos: Flow<List<VideoPostEntity>> = dao.getAllVideos()

  fun getCreatorVideos(creatorId: Long): Flow<List<VideoPostEntity>> = dao.getCreatorVideos(creatorId)

  fun getCommentsForVideo(videoId: Long): Flow<List<VideoCommentEntity>> = dao.getCommentsForVideo(videoId)

  suspend fun toggleLikeVideo(video: VideoPostEntity) {
    val newLiked = !video.isLikedByMe
    val delta = if (newLiked) 1 else -1
    dao.updateVideoLike(video.id, newLiked, delta)
  }

  suspend fun recordVideoView(videoId: Long) {
    dao.incrementVideoView(videoId)
  }

  suspend fun addVideoComment(videoId: Long, text: String) {
    val user = dao.getCurrentUser() ?: return
    val comment = VideoCommentEntity(
      videoId = videoId,
      authorName = user.displayName,
      authorAvatar = user.avatarUrl,
      text = text
    )
    dao.insertVideoComment(comment)
  }

  suspend fun uploadVideo(
    title: String,
    description: String,
    category: String,
    durationSec: Int
  ) {
    val user = dao.getCurrentUser() ?: return
    val post = VideoPostEntity(
      creatorId = user.id,
      creatorName = user.displayName,
      creatorAvatar = user.avatarUrl,
      title = title,
      description = description,
      category = category,
      durationSec = durationSec,
      likesCount = 0,
      commentsCount = 0,
      viewsCount = 1,
      isLikedByMe = false,
      isMonetized = true,
      earnedGhs = 0.50
    )
    dao.insertVideo(post)
  }

  // Creator Payouts & Mobile Money
  val allPayouts: Flow<List<PayoutRequestEntity>> = dao.getAllPayoutRequests()

  suspend fun requestPayout(
    amountGhs: Double,
    provider: String,
    momoNumber: String,
    accountName: String
  ): Boolean {
    val user = dao.getCurrentUser() ?: return false
    if (user.balanceGhs < amountGhs || amountGhs < 20.0) {
      return false
    }
    // Deduct user balance
    dao.deductBalance(user.id, amountGhs)

    val payout = PayoutRequestEntity(
      creatorId = user.id,
      amountGhs = amountGhs,
      provider = provider,
      momoNumber = momoNumber,
      accountName = accountName,
      status = "PROCESSING"
    )
    dao.insertPayoutRequest(payout)
    return true
  }
}
