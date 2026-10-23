package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
  entities = [
    UserEntity::class,
    SubjectEntity::class,
    LessonEntity::class,
    QuestionEntity::class,
    QuizAttemptEntity::class,
    ChatGroupEntity::class,
    ChatMessageEntity::class,
    VideoPostEntity::class,
    VideoCommentEntity::class,
    PayoutRequestEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun appDao(): AppDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "fty_database.db"
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}
