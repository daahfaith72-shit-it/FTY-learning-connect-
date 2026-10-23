package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface AppDao {
  // User operations
  @Query("SELECT * FROM users LIMIT 1")
  fun getCurrentUserFlow(): Flow<UserEntity?>

  @Query("SELECT * FROM users LIMIT 1")
  suspend fun getCurrentUser(): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity): Long

  @Update
  suspend fun updateUser(user: UserEntity)

  @Query("UPDATE users SET isCreator = 1, role = 'CREATOR' WHERE id = :userId")
  suspend fun unlockCreatorMode(userId: Long)

  @Query("UPDATE users SET balanceGhs = balanceGhs - :amount WHERE id = :userId")
  suspend fun deductBalance(userId: Long, amount: Double)

  @Query("UPDATE users SET points = points + :addPoints, quizzesPassed = quizzesPassed + 1 WHERE id = :userId")
  suspend fun recordQuizPassed(userId: Long, addPoints: Int)

  // Education: Subjects
  @Query("SELECT * FROM subjects ORDER BY id ASC")
  fun getAllSubjects(): Flow<List<SubjectEntity>>

  @Query("SELECT * FROM subjects WHERE gradeLevel = :level ORDER BY id ASC")
  fun getSubjectsByLevel(level: String): Flow<List<SubjectEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSubject(subject: SubjectEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSubjects(subjects: List<SubjectEntity>)

  // Education: Lessons
  @Query("SELECT * FROM lessons WHERE subjectId = :subjectId ORDER BY id ASC")
  fun getLessonsBySubject(subjectId: Long): Flow<List<LessonEntity>>

  @Query("SELECT * FROM lessons WHERE gradeLevel = :level ORDER BY id ASC")
  fun getLessonsByLevel(level: String): Flow<List<LessonEntity>>

  @Query("SELECT * FROM lessons WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%'")
  fun searchLessons(query: String): Flow<List<LessonEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLesson(lesson: LessonEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLessons(lessons: List<LessonEntity>)

  @Query("UPDATE lessons SET isCompleted = 1 WHERE id = :lessonId")
  suspend fun markLessonCompleted(lessonId: Long)

  // Education: Questions
  @Query("SELECT * FROM questions WHERE subjectId = :subjectId ORDER BY id ASC")
  suspend fun getQuestionsBySubject(subjectId: Long): List<QuestionEntity>

  @Query("SELECT * FROM questions WHERE gradeLevel = :level ORDER BY id ASC")
  suspend fun getQuestionsByLevel(level: String): List<QuestionEntity>

  @Query("SELECT * FROM questions WHERE gradeLevel = :level AND isAdvanced = 1")
  suspend fun getAdvancedQuestionsByLevel(level: String): List<QuestionEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestions(questions: List<QuestionEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestion(question: QuestionEntity): Long

  // Quiz Attempts
  @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
  fun getAllQuizAttempts(): Flow<List<QuizAttemptEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuizAttempt(attempt: QuizAttemptEntity): Long

  // Chat Groups
  @Query("SELECT * FROM chat_groups ORDER BY id ASC")
  fun getAllChatGroups(): Flow<List<ChatGroupEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChatGroups(groups: List<ChatGroupEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChatGroup(group: ChatGroupEntity): Long

  // Chat Messages
  @Query("SELECT * FROM chat_messages WHERE groupId = :groupId AND isFlagged = 0 ORDER BY timestamp ASC")
  fun getGroupMessages(groupId: Long): Flow<List<ChatMessageEntity>>

  @Query("SELECT * FROM chat_messages WHERE isPrivate = 1 AND isFlagged = 0 ORDER BY timestamp ASC")
  fun getPrivateMessages(): Flow<List<ChatMessageEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessage(message: ChatMessageEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessages(messages: List<ChatMessageEntity>)

  @Query("UPDATE chat_messages SET isFlagged = 1 WHERE id = :messageId")
  suspend fun flagMessage(messageId: Long)

  // Videos
  @Query("SELECT * FROM video_posts ORDER BY createdAt DESC")
  fun getAllVideos(): Flow<List<VideoPostEntity>>

  @Query("SELECT * FROM video_posts WHERE creatorId = :creatorId ORDER BY createdAt DESC")
  fun getCreatorVideos(creatorId: Long): Flow<List<VideoPostEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVideo(video: VideoPostEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVideos(videos: List<VideoPostEntity>)

  @Query("UPDATE video_posts SET isLikedByMe = :liked, likesCount = likesCount + :delta WHERE id = :videoId")
  suspend fun updateVideoLike(videoId: Long, liked: Boolean, delta: Int)

  @Query("UPDATE video_posts SET viewsCount = viewsCount + 1, earnedGhs = earnedGhs + 0.05 WHERE id = :videoId")
  suspend fun incrementVideoView(videoId: Long)

  // Video Comments
  @Query("SELECT * FROM video_comments WHERE videoId = :videoId ORDER BY timestamp ASC")
  fun getCommentsForVideo(videoId: Long): Flow<List<VideoCommentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVideoComment(comment: VideoCommentEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVideoComments(comments: List<VideoCommentEntity>)

  // Payouts
  @Query("SELECT * FROM payout_requests ORDER BY requestedAt DESC")
  fun getAllPayoutRequests(): Flow<List<PayoutRequestEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPayoutRequest(payout: PayoutRequestEntity): Long
}
