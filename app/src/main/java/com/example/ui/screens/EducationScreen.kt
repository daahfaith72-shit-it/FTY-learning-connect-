package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LessonEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.SubjectEntity
import com.example.ui.theme.SkyBlueExtraLight
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SkyBluePrimaryDark
import com.example.ui.viewmodel.FtyViewModel

@Composable
fun EducationScreen(
  viewModel: FtyViewModel,
  onOpenCalculator: () -> Unit,
  modifier: Modifier = Modifier
) {
  val selectedGrade by viewModel.selectedGradeLevel.collectAsStateWithLifecycle()
  val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
  val selectedLesson by viewModel.selectedLesson.collectAsStateWithLifecycle()
  val subjects by viewModel.subjectsForGrade.collectAsStateWithLifecycle()
  val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
  val quizFinished by viewModel.quizFinished.collectAsStateWithLifecycle()

  var isQuizActive by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("education_screen")
  ) {
    when {
      isQuizActive && quizQuestions.isNotEmpty() -> {
        QuizScreenContent(
          viewModel = viewModel,
          questions = quizQuestions,
          isFinished = quizFinished,
          onCloseQuiz = {
            isQuizActive = false
          }
        )
      }
      selectedLesson != null -> {
        LessonReaderContent(
          lesson = selectedLesson!!,
          onBack = { viewModel.clearSelectedLesson() },
          onComplete = { viewModel.completeCurrentLesson() },
          onStartQuiz = {
            viewModel.startQuiz(subjectId = selectedLesson!!.subjectId, gradeLevel = selectedLesson!!.gradeLevel)
            isQuizActive = true
          }
        )
      }
      selectedSubject != null -> {
        SubjectDetailContent(
          subject = selectedSubject!!,
          onBack = { viewModel.clearSelectedSubject() },
          onSelectLesson = { lesson -> viewModel.selectLesson(lesson) },
          onStartStandardQuiz = {
            viewModel.startQuiz(subjectId = selectedSubject!!.id, gradeLevel = selectedSubject!!.gradeLevel, advanced = false)
            isQuizActive = true
          },
          onStartAdvancedQuiz = {
            viewModel.startQuiz(subjectId = selectedSubject!!.id, gradeLevel = selectedSubject!!.gradeLevel, advanced = true)
            isQuizActive = true
          }
        )
      }
      else -> {
        SubjectCatalogContent(
          viewModel = viewModel,
          selectedGrade = selectedGrade,
          subjects = subjects,
          onSelectSubject = { sub -> viewModel.selectSubject(sub) },
          onOpenCalculator = onOpenCalculator
        )
      }
    }
  }
}

@Composable
private fun SubjectCatalogContent(
  viewModel: FtyViewModel,
  selectedGrade: String,
  subjects: List<SubjectEntity>,
  onSelectSubject: (SubjectEntity) -> Unit,
  onOpenCalculator: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  val gradeLevels = listOf("Primary", "JHS", "SHS")

  val filteredSubjects = if (searchQuery.isBlank()) {
    subjects
  } else {
    subjects.filter {
      it.title.contains(searchQuery, ignoreCase = true) ||
          it.description.contains(searchQuery, ignoreCase = true)
    }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header & Calculator quick shortcut
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "FTY Learning Hub",
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              color = SkyBluePrimaryDark
            )
          )
          Text(
            text = "Choose your level to access tailored curricula",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = SkyBlueLight,
          modifier = Modifier.clickable(onClick = onOpenCalculator)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Calculate, contentDescription = null, tint = SkyBluePrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Calc",
              color = SkyBluePrimaryDark,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
        }
      }
    }

    // Grade Level Selector Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        gradeLevels.forEach { level ->
          val isSelected = selectedGrade == level
          val label = when (level) {
            "Primary" -> "Primary School"
            "JHS" -> "Junior High (JHS)"
            "SHS" -> "Senior High (SHS)"
            else -> level
          }

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isSelected) SkyBluePrimary else Color.White,
            shadowElevation = if (isSelected) 3.dp else 1.dp,
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .clickable { viewModel.selectGradeLevel(level) }
              .testTag("grade_chip_$level")
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = label,
                color = if (isSelected) Color.White else Color(0xFF334155),
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp
                )
              )
            }
          }
        }
      }
    }

    // Search Field
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search subjects, formulas, or topics...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SkyBluePrimary) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Close, contentDescription = "Clear")
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("education_search_input"),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = SkyBluePrimary,
          unfocusedBorderColor = Color(0xFFE2E8F0)
        ),
        singleLine = true
      )
    }

    // Subjects Title
    item {
      Text(
        text = "$selectedGrade Curricula & Subjects (${filteredSubjects.size})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = SkyBluePrimaryDark
      )
    }

    // Subject Cards List
    items(filteredSubjects, key = { it.id }) { subject ->
      SubjectCard(subject = subject, onClick = { onSelectSubject(subject) })
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
private fun SubjectCard(
  subject: SubjectEntity,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("subject_card_${subject.code}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(SkyBlueLight),
        contentAlignment = Alignment.Center
      ) {
        val icon = when (subject.gradeLevel) {
          "Primary" -> Icons.Default.MenuBook
          "JHS" -> Icons.Default.Biotech
          else -> Icons.Default.School
        }
        Icon(icon, contentDescription = null, tint = SkyBluePrimaryDark, modifier = Modifier.size(28.dp))
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = subject.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
          )
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = SkyBlueExtraLight
          ) {
            Text(
              text = subject.code,
              color = SkyBluePrimary,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = subject.description,
          style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)),
          maxLines = 2
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Quiz, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${subject.lessonCount} Lessons & Practice Quizzes",
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF10B981), fontWeight = FontWeight.SemiBold)
          )
        }
      }
    }
  }
}

@Composable
private fun SubjectDetailContent(
  subject: SubjectEntity,
  onBack: () -> Unit,
  onSelectLesson: (LessonEntity) -> Unit,
  onStartStandardQuiz: () -> Unit,
  onStartAdvancedQuiz: () -> Unit
) {
  // Mock lessons for this subject if not queried
  val sampleLessons = listOf(
    LessonEntity(
      id = 101,
      subjectId = subject.id,
      gradeLevel = subject.gradeLevel,
      classGrade = "Unit 1",
      title = "Foundations and Core Principles of ${subject.title}",
      summary = "Key definitions, core theory, formulas, and real-world applications in Ghana.",
      content = "Detailed walkthrough with step-by-step examples and visual memory aids.",
      keyTakeaways = "Master the primary definitions and remember fundamental equations.",
      durationMin = 15,
      difficulty = "Standard"
    ),
    LessonEntity(
      id = 102,
      subjectId = subject.id,
      gradeLevel = subject.gradeLevel,
      classGrade = "Unit 2",
      title = "Applied Problem Solving & Exam Tactics",
      summary = "How to break down challenging questions and secure full marks in exams.",
      content = "Techniques for factorisation, balancing, and proof generation.",
      keyTakeaways = "Show all intermediate working and double-check final arithmetic.",
      durationMin = 20,
      difficulty = "Advanced"
    )
  )

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SkyBluePrimaryDark)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
          Text(
            text = subject.title,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              color = SkyBluePrimaryDark
            )
          )
          Text(
            text = "${subject.gradeLevel} Level • ${subject.code}",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
          )
        }
      }
    }

    // Action Quiz Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SkyBlueLight)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Interactive Practice & Quizzes",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = SkyBluePrimaryDark
            )
          )
          Text(
            text = "Test your grasp with instant automatic marking and detailed step-by-step explanations.",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569))
          )
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onStartStandardQuiz,
              modifier = Modifier
                .weight(1f)
                .testTag("start_standard_quiz_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Standard Quiz")
            }

            Button(
              onClick = onStartAdvancedQuiz,
              modifier = Modifier
                .weight(1f)
                .testTag("start_advanced_quiz_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Advanced ★")
            }
          }
        }
      }
    }

    item {
      Text(
        text = "Curriculum Units & Lessons",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF0F172A)
      )
    }

    items(sampleLessons) { lesson ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onSelectLesson(lesson) }
          .testTag("lesson_item_${lesson.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = lesson.title,
              style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF0F172A),
              modifier = Modifier.weight(1f)
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (lesson.difficulty == "Advanced") Color(0xFFFEF3C7) else SkyBlueExtraLight
            ) {
              Text(
                text = lesson.difficulty,
                color = if (lesson.difficulty == "Advanced") Color(0xFFD97706) else SkyBluePrimary,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = lesson.summary,
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Duration: ~${lesson.durationMin} mins • Click to read",
            style = MaterialTheme.typography.labelSmall.copy(color = SkyBluePrimary, fontWeight = FontWeight.SemiBold)
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
private fun LessonReaderContent(
  lesson: LessonEntity,
  onBack: () -> Unit,
  onComplete: () -> Unit,
  onStartQuiz: () -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SkyBluePrimaryDark)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Lesson Material",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            color = SkyBluePrimaryDark
          )
        )
      }
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = lesson.title,
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              color = SkyBluePrimaryDark
            )
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Target Level: ${lesson.gradeLevel} • ${lesson.classGrade}",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Body Content
          Text(
            text = lesson.content,
            style = MaterialTheme.typography.bodyMedium.copy(
              color = Color(0xFF1E293B),
              lineHeight = 24.sp
            )
          )

          Spacer(modifier = Modifier.height(18.dp))

          // Key Takeaways Box
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SkyBlueExtraLight)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = SkyBluePrimary)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Key Takeaways",
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SkyBluePrimaryDark
                  )
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = lesson.keyTakeaways,
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155))
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onComplete,
              modifier = Modifier
                .weight(1f)
                .testTag("mark_lesson_completed_btn"),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (lesson.isCompleted) Color(0xFF10B981) else SkyBluePrimary
              ),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (lesson.isCompleted) "Completed ✓" else "Mark Done")
            }

            Button(
              onClick = onStartQuiz,
              modifier = Modifier
                .weight(1f)
                .testTag("take_lesson_quiz_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0369A1)),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Take Quiz")
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
private fun QuizScreenContent(
  viewModel: FtyViewModel,
  questions: List<QuestionEntity>,
  isFinished: Boolean,
  onCloseQuiz: () -> Unit
) {
  val currentIndex by viewModel.quizQuestionIndex.collectAsStateWithLifecycle()
  val selectedAnswer by viewModel.selectedAnswerIndex.collectAsStateWithLifecycle()
  val isSubmitted by viewModel.isAnswerSubmitted.collectAsStateWithLifecycle()
  val score by viewModel.quizCurrentScore.collectAsStateWithLifecycle()
  val result by viewModel.latestQuizResult.collectAsStateWithLifecycle()
  val isAdvanced by viewModel.isAdvancedQuiz.collectAsStateWithLifecycle()

  val currentQuestion = questions.getOrNull(currentIndex)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Top Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onCloseQuiz) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = SkyBluePrimaryDark)
        }
        Text(
          text = if (isAdvanced) "Advanced Challenge ★" else "Practice Quiz",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = if (isAdvanced) Color(0xFFD97706) else SkyBluePrimaryDark
          )
        )
      }

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = SkyBlueLight
      ) {
        Text(
          text = "Score: $score",
          color = SkyBluePrimaryDark,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }
    }

    if (isFinished) {
      // Final Results Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 20.dp)
          .testTag("quiz_results_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          val passed = result?.passed ?: (score >= (questions.size / 2))
          val percentage = result?.percentage ?: if (questions.isNotEmpty()) (score * 100) / questions.size else 0

          Icon(
            if (passed) Icons.Default.EmojiEvents else Icons.Default.HelpOutline,
            contentDescription = null,
            tint = if (passed) Color(0xFF10B981) else Color(0xFFF59E0B),
            modifier = Modifier.size(64.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = if (passed) "Outstanding Achievement!" else "Good Effort, Keep Practicing!",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              color = if (passed) Color(0xFF10B981) else Color(0xFFD97706)
            )
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "You scored $score out of ${questions.size} ($percentage%)",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
          )

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = if (passed) "+${score * 10} XP added to your FTY Learning Profile!" else "Review the explanations to master these concepts.",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
          )

          Spacer(modifier = Modifier.height(24.dp))
          Button(
            onClick = onCloseQuiz,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("quiz_finish_return_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Back to Lessons", fontWeight = FontWeight.Bold)
          }
        }
      }
      Spacer(modifier = Modifier.height(60.dp))
    } else if (currentQuestion != null) {
      // Question Card
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Progress Indicator
        val progress = (currentIndex + 1).toFloat() / questions.size.toFloat()
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = SkyBluePrimary,
          trackColor = SkyBlueLight
        )

        Text(
          text = "Question ${currentIndex + 1} of ${questions.size}",
          style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )

        Text(
          text = currentQuestion.questionText,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            lineHeight = 24.sp
          )
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Options A, B, C, D
        val options = listOf(
          currentQuestion.optionA,
          currentQuestion.optionB,
          currentQuestion.optionC,
          currentQuestion.optionD
        )

        options.forEachIndexed { index, optionText ->
          val isThisSelected = selectedAnswer == index
          val isCorrect = index == currentQuestion.correctOptionIndex

          val cardColor = when {
            !isSubmitted && isThisSelected -> SkyBlueLight
            isSubmitted && isCorrect -> Color(0xFFDCFCE7) // green
            isSubmitted && isThisSelected && !isCorrect -> Color(0xFFFEE2E2) // red
            else -> Color.White
          }

          val borderColor = when {
            !isSubmitted && isThisSelected -> SkyBluePrimary
            isSubmitted && isCorrect -> Color(0xFF16A34A)
            isSubmitted && isThisSelected && !isCorrect -> Color(0xFFDC2626)
            else -> Color(0xFFE2E8F0)
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = cardColor,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { viewModel.chooseAnswer(index) }
              .testTag("quiz_option_$index")
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              val optionLabel = listOf("A", "B", "C", "D")[index]
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(if (isThisSelected) SkyBluePrimary else SkyBlueLight),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = optionLabel,
                  color = if (isThisSelected) Color.White else SkyBluePrimaryDark,
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = optionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = if (isThisSelected) FontWeight.Bold else FontWeight.Normal,
                  color = Color(0xFF1E293B)
                )
              )
            }
          }
        }

        // Automatic Marking Explanation Box
        if (isSubmitted) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("quiz_explanation_box"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SkyBlueExtraLight)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "Explanation & Key Insight:",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = SkyBluePrimaryDark
                )
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = currentQuestion.explanation,
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155))
              )
            }
          }
        }
      }

      // Bottom Action Button
      Button(
        onClick = {
          if (!isSubmitted) {
            viewModel.submitAnswer()
          } else {
            viewModel.nextQuestion()
          }
        },
        enabled = selectedAnswer != null,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("quiz_submit_or_next_btn"),
        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text(
          text = if (!isSubmitted) "Submit Answer" else if (currentIndex + 1 < questions.size) "Next Question →" else "View Results",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }
    }
  }
}
