package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
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
import com.example.data.repository.FtyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class FtyViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: FtyRepository

  val currentUser: StateFlow<UserEntity?>
  val allVideos: StateFlow<List<VideoPostEntity>>
  val chatGroups: StateFlow<List<ChatGroupEntity>>
  val quizAttempts: StateFlow<List<QuizAttemptEntity>>
  val allPayouts: StateFlow<List<PayoutRequestEntity>>

  // Education state
  private val _selectedGradeLevel = MutableStateFlow("SHS")
  val selectedGradeLevel = _selectedGradeLevel.asStateFlow()

  private val _selectedSubject = MutableStateFlow<SubjectEntity?>(null)
  val selectedSubject = _selectedSubject.asStateFlow()

  private val _selectedLesson = MutableStateFlow<LessonEntity?>(null)
  val selectedLesson = _selectedLesson.asStateFlow()

  private val _lessonSearchQuery = MutableStateFlow("")
  val lessonSearchQuery = _lessonSearchQuery.asStateFlow()

  val subjectsForGrade: StateFlow<List<SubjectEntity>>
  private val _lessonsForSelectedSubject = MutableStateFlow<List<LessonEntity>>(emptyList())
  val lessonsForSelectedSubject = _lessonsForSelectedSubject.asStateFlow()

  private val _searchResults = MutableStateFlow<List<LessonEntity>>(emptyList())
  val searchResults = _searchResults.asStateFlow()

  // Quiz state
  private val _quizQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
  val quizQuestions = _quizQuestions.asStateFlow()

  private val _quizQuestionIndex = MutableStateFlow(0)
  val quizQuestionIndex = _quizQuestionIndex.asStateFlow()

  private val _selectedAnswerIndex = MutableStateFlow<Int?>(null)
  val selectedAnswerIndex = _selectedAnswerIndex.asStateFlow()

  private val _isAnswerSubmitted = MutableStateFlow(false)
  val isAnswerSubmitted = _isAnswerSubmitted.asStateFlow()

  private val _quizCurrentScore = MutableStateFlow(0)
  val quizCurrentScore = _quizCurrentScore.asStateFlow()

  private val _quizFinished = MutableStateFlow(false)
  val quizFinished = _quizFinished.asStateFlow()

  private val _latestQuizResult = MutableStateFlow<QuizAttemptEntity?>(null)
  val latestQuizResult = _latestQuizResult.asStateFlow()

  private val _isAdvancedQuiz = MutableStateFlow(false)
  val isAdvancedQuiz = _isAdvancedQuiz.asStateFlow()

  // Chat state
  private val _selectedGroup = MutableStateFlow<ChatGroupEntity?>(null)
  val selectedGroup = _selectedGroup.asStateFlow()

  private val _groupMessages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())
  val groupMessages = _groupMessages.asStateFlow()
  val privateMessages: StateFlow<List<ChatMessageEntity>>

  // Video state
  private val _activeCommentVideo = MutableStateFlow<VideoPostEntity?>(null)
  val activeCommentVideo = _activeCommentVideo.asStateFlow()

  private val _activeVideoComments = MutableStateFlow<List<VideoCommentEntity>>(emptyList())
  val activeVideoComments = _activeVideoComments.asStateFlow()

  // Creator state
  private val _creatorPinMessage = MutableStateFlow<String?>(null)
  val creatorPinMessage = _creatorPinMessage.asStateFlow()

  // Calculator state
  private val _calcExpression = MutableStateFlow("")
  val calcExpression = _calcExpression.asStateFlow()

  private val _calcResult = MutableStateFlow("0")
  val calcResult = _calcResult.asStateFlow()

  private val _isDegree = MutableStateFlow(true)
  val isDegree = _isDegree.asStateFlow()

  init {
    val dao = AppDatabase.getDatabase(application).appDao()
    repository = FtyRepository(dao)

    viewModelScope.launch {
      repository.initializeData()
    }

    currentUser = repository.currentUser.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      null
    )

    allVideos = repository.allVideos.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    chatGroups = repository.chatGroups.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    quizAttempts = repository.quizAttempts.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    allPayouts = repository.allPayouts.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    privateMessages = repository.privateMessages.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    // Dynamic subjects for current grade level
    subjectsForGrade = combine(_selectedGradeLevel, repository.getAllSubjects()) { grade, all ->
      all.filter { it.gradeLevel == grade }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  }

  // --- Education Actions ---
  fun selectGradeLevel(level: String) {
    _selectedGradeLevel.value = level
    _selectedSubject.value = null
    _selectedLesson.value = null
  }

  fun selectSubject(subject: SubjectEntity) {
    _selectedSubject.value = subject
    _selectedLesson.value = null
  }

  fun clearSelectedSubject() {
    _selectedSubject.value = null
    _selectedLesson.value = null
  }

  fun selectLesson(lesson: LessonEntity) {
    _selectedLesson.value = lesson
  }

  fun clearSelectedLesson() {
    _selectedLesson.value = null
  }

  fun completeCurrentLesson() {
    val lesson = _selectedLesson.value ?: return
    viewModelScope.launch {
      repository.markLessonCompleted(lesson.id)
      _selectedLesson.value = lesson.copy(isCompleted = true)
    }
  }

  fun updateSearchQuery(query: String) {
    _lessonSearchQuery.value = query
  }

  // --- Quiz Actions ---
  fun startQuiz(subjectId: Long? = null, gradeLevel: String = _selectedGradeLevel.value, advanced: Boolean = false) {
    _isAdvancedQuiz.value = advanced
    viewModelScope.launch {
      val questions = if (subjectId != null) {
        repository.getQuestionsForSubject(subjectId).let { list ->
          if (advanced) list.filter { it.isAdvanced } else list
        }
      } else {
        repository.getQuestionsForLevel(gradeLevel, advanced)
      }
      _quizQuestions.value = questions.ifEmpty {
        // Fallback default questions if none matched
        repository.getQuestionsForLevel("SHS", false)
      }
      _quizQuestionIndex.value = 0
      _selectedAnswerIndex.value = null
      _isAnswerSubmitted.value = false
      _quizCurrentScore.value = 0
      _quizFinished.value = false
      _latestQuizResult.value = null
    }
  }

  fun chooseAnswer(optionIndex: Int) {
    if (_isAnswerSubmitted.value) return
    _selectedAnswerIndex.value = optionIndex
  }

  fun submitAnswer() {
    val selected = _selectedAnswerIndex.value ?: return
    val currentQ = _quizQuestions.value.getOrNull(_quizQuestionIndex.value) ?: return

    _isAnswerSubmitted.value = true
    if (selected == currentQ.correctOptionIndex) {
      _quizCurrentScore.value += 1
    }
  }

  fun nextQuestion() {
    val nextIndex = _quizQuestionIndex.value + 1
    val total = _quizQuestions.value.size
    if (nextIndex < total) {
      _quizQuestionIndex.value = nextIndex
      _selectedAnswerIndex.value = null
      _isAnswerSubmitted.value = false
    } else {
      // Quiz Finished! Record attempt
      finishQuiz()
    }
  }

  private fun finishQuiz() {
    _quizFinished.value = true
    val subjectTitle = _selectedSubject.value?.title ?: "FTY Knowledge Challenge"
    val score = _quizCurrentScore.value
    val total = _quizQuestions.value.size
    viewModelScope.launch {
      val result = repository.recordQuizResult(
        subjectTitle = subjectTitle,
        gradeLevel = _selectedGradeLevel.value,
        score = score,
        total = total
      )
      _latestQuizResult.value = result
    }
  }

  // --- Chat Actions ---
  fun selectChatGroup(group: ChatGroupEntity) {
    _selectedGroup.value = group
  }

  fun sendGroupMessage(groupId: Long, text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      repository.sendGroupMessage(groupId, text.trim())
    }
  }

  fun sendPrivateMessage(recipientId: Long, text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      repository.sendPrivateMessage(recipientId, text.trim())
    }
  }

  fun flagMessage(messageId: Long) {
    viewModelScope.launch {
      repository.reportMessage(messageId)
    }
  }

  fun createNewGroup(name: String, desc: String, grade: String) {
    viewModelScope.launch {
      repository.createChatGroup(name, desc, grade, "group")
    }
  }

  // --- Video Actions ---
  fun toggleLikeVideo(video: VideoPostEntity) {
    viewModelScope.launch {
      repository.toggleLikeVideo(video)
    }
  }

  fun viewVideo(video: VideoPostEntity) {
    viewModelScope.launch {
      repository.recordVideoView(video.id)
    }
  }

  fun openComments(video: VideoPostEntity) {
    _activeCommentVideo.value = video
  }

  fun closeComments() {
    _activeCommentVideo.value = null
  }

  fun submitComment(videoId: Long, text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      repository.addVideoComment(videoId, text.trim())
    }
  }

  fun uploadVideo(title: String, desc: String, category: String, durationSec: Int) {
    viewModelScope.launch {
      repository.uploadVideo(title, desc, category, durationSec)
    }
  }

  // --- Creator & Monetization Actions ---
  fun unlockCreatorWithPin(pin: String, onResult: (Boolean) -> Unit) {
    viewModelScope.launch {
      val success = repository.verifyAndUnlockCreator(pin)
      if (success) {
        _creatorPinMessage.value = "Welcome Creator! PIN verified successfully."
      } else {
        _creatorPinMessage.value = "Incorrect PIN. Access denied."
      }
      onResult(success)
    }
  }

  fun clearPinMessage() {
    _creatorPinMessage.value = null
  }

  fun requestCashout(
    amountGhs: Double,
    provider: String,
    momoNumber: String,
    accountName: String,
    onResult: (Boolean, String) -> Unit
  ) {
    viewModelScope.launch {
      val user = currentUser.value
      if (user == null) {
        onResult(false, "User not loaded")
        return@launch
      }
      if (user.balanceGhs < amountGhs) {
        onResult(false, "Insufficient balance. Available: GHS ${String.format("%.2f", user.balanceGhs)}")
        return@launch
      }
      if (amountGhs < 20.0) {
        onResult(false, "Minimum cashout amount is GHS 20.00")
        return@launch
      }
      if (momoNumber.length < 10) {
        onResult(false, "Please enter a valid 10-digit Ghana Mobile Money number")
        return@launch
      }
      val ok = repository.requestPayout(amountGhs, provider, momoNumber, accountName)
      if (ok) {
        onResult(true, "Payout request for GHS ${String.format("%.2f", amountGhs)} submitted to $provider ($momoNumber). Processing initiated.")
      } else {
        onResult(false, "Unable to complete cashout. Please check requirements.")
      }
    }
  }

  fun addNewLesson(
    subjectId: Long,
    gradeLevel: String,
    classGrade: String,
    title: String,
    summary: String,
    content: String,
    takeaways: String
  ) {
    viewModelScope.launch {
      val lesson = LessonEntity(
        subjectId = subjectId,
        gradeLevel = gradeLevel,
        classGrade = classGrade,
        title = title,
        summary = summary,
        content = content,
        keyTakeaways = takeaways,
        durationMin = 15,
        difficulty = "Standard"
      )
      repository.addNewLesson(lesson)
    }
  }

  fun addNewQuizQuestion(
    subjectId: Long,
    gradeLevel: String,
    questionText: String,
    optA: String,
    optB: String,
    optC: String,
    optD: String,
    correctIdx: Int,
    explanation: String,
    isAdvanced: Boolean
  ) {
    viewModelScope.launch {
      val question = QuestionEntity(
        subjectId = subjectId,
        lessonId = 0,
        gradeLevel = gradeLevel,
        questionText = questionText,
        optionA = optA,
        optionB = optB,
        optionC = optC,
        optionD = optD,
        correctOptionIndex = correctIdx,
        explanation = explanation,
        isAdvanced = isAdvanced
      )
      repository.addNewQuestion(question)
    }
  }

  fun updateProfile(name: String, bio: String, grade: String) {
    viewModelScope.launch {
      repository.updateUserProfile(name, bio, grade)
    }
  }

  // --- Scientific Calculator Functions ---
  fun onCalcInput(token: String) {
    when (token) {
      "C" -> {
        _calcExpression.value = ""
        _calcResult.value = "0"
      }
      "⌫" -> {
        if (_calcExpression.value.isNotEmpty()) {
          _calcExpression.value = _calcExpression.value.dropLast(1)
        }
      }
      "=" -> evaluateCalculator()
      "DEG/RAD" -> _isDegree.value = !_isDegree.value
      "sin", "cos", "tan", "ln", "log", "√" -> {
        _calcExpression.value = _calcExpression.value + "$token("
      }
      "π" -> _calcExpression.value = _calcExpression.value + "3.14159"
      "e" -> _calcExpression.value = _calcExpression.value + "2.71828"
      else -> {
        _calcExpression.value = _calcExpression.value + token
      }
    }
  }

  private fun evaluateCalculator() {
    val expr = _calcExpression.value
    if (expr.isBlank()) return
    try {
      val res = evaluateSimpleExpression(expr, _isDegree.value)
      _calcResult.value = if (res % 1.0 == 0.0) {
        res.toLong().toString()
      } else {
        String.format("%.6f", res).trimEnd('0').trimEnd('.')
      }
    } catch (e: Exception) {
      _calcResult.value = "Error"
    }
  }

  private fun evaluateSimpleExpression(expr: String, inDegrees: Boolean): Double {
    // Quick robust parser for numbers, +, -, *, /, powers, and functions
    var sanitized = expr.replace("×", "*").replace("÷", "/")
    
    // Evaluate functions
    val sinRegex = Regex("sin\\(([^()]+)\\)")
    sanitized = sinRegex.replace(sanitized) { match ->
      val v = match.groupValues[1].toDoubleOrNull() ?: 0.0
      val angle = if (inDegrees) Math.toRadians(v) else v
      sin(angle).toString()
    }

    val cosRegex = Regex("cos\\(([^()]+)\\)")
    sanitized = cosRegex.replace(sanitized) { match ->
      val v = match.groupValues[1].toDoubleOrNull() ?: 0.0
      val angle = if (inDegrees) Math.toRadians(v) else v
      cos(angle).toString()
    }

    val tanRegex = Regex("tan\\(([^()]+)\\)")
    sanitized = tanRegex.replace(sanitized) { match ->
      val v = match.groupValues[1].toDoubleOrNull() ?: 0.0
      val angle = if (inDegrees) Math.toRadians(v) else v
      tan(angle).toString()
    }

    val sqrtRegex = Regex("√\\(([^()]+)\\)")
    sanitized = sqrtRegex.replace(sanitized) { match ->
      val v = match.groupValues[1].toDoubleOrNull() ?: 0.0
      sqrt(v).toString()
    }

    val logRegex = Regex("log\\(([^()]+)\\)")
    sanitized = logRegex.replace(sanitized) { match ->
      val v = match.groupValues[1].toDoubleOrNull() ?: 1.0
      log10(v).toString()
    }

    val lnRegex = Regex("ln\\(([^()]+)\\)")
    sanitized = lnRegex.replace(sanitized) { match ->
      val v = match.groupValues[1].toDoubleOrNull() ?: 1.0
      ln(v).toString()
    }

    // Now evaluate basic arithmetic
    return ExpressionParser(sanitized).parse()
  }
}

private class ExpressionParser(private val clean: String) {
  private var pos = -1
  private var ch = -1

  private fun nextChar() {
    ch = if (++pos < clean.length) clean[pos].code else -1
  }

  private fun eat(charToEat: Int): Boolean {
    while (ch == ' '.code) nextChar()
    if (ch == charToEat) {
      nextChar()
      return true
    }
    return false
  }

  fun parse(): Double {
    nextChar()
    return parseExpression()
  }

  private fun parseExpression(): Double {
    var x = parseTerm()
    while (true) {
      when {
        eat('+'.code) -> x += parseTerm()
        eat('-'.code) -> x -= parseTerm()
        else -> return x
      }
    }
  }

  private fun parseTerm(): Double {
    var x = parseFactor()
    while (true) {
      when {
        eat('*'.code) -> x *= parseFactor()
        eat('/'.code) -> {
          val denom = parseFactor()
          x = if (denom != 0.0) x / denom else Double.NaN
        }
        eat('%'.code) -> x %= parseFactor()
        else -> return x
      }
    }
  }

  private fun parseFactor(): Double {
    if (eat('+'.code)) return parseFactor()
    if (eat('-'.code)) return -parseFactor()

    var x: Double
    val startPos = pos
    if (eat('('.code)) {
      x = parseExpression()
      eat(')'.code)
    } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) {
      while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
      x = clean.substring(startPos, pos).toDouble()
    } else {
      return 0.0
    }

    if (eat('^'.code)) x = Math.pow(x, parseFactor())
    return x
  }
}
