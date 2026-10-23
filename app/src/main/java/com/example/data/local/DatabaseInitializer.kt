package com.example.data.local

import com.example.data.model.ChatGroupEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.LessonEntity
import com.example.data.model.PayoutRequestEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.UserEntity
import com.example.data.model.VideoCommentEntity
import com.example.data.model.VideoPostEntity

object DatabaseInitializer {

  suspend fun populateIfEmpty(dao: AppDao) {
    if (dao.getCurrentUser() != null) return

    // 1. Initial User
    val initialUser = UserEntity(
      id = 1,
      username = "tembilla_learner",
      displayName = "Faith Yenpang",
      email = "faith@ftyacademy.org",
      bio = "Lifelong learner, tech enthusiast, and advocate for accessible digital education in Ghana.",
      avatarUrl = "",
      gradeLevel = "SHS",
      role = "STUDENT",
      isCreator = false,
      creatorPin = "faith1132A",
      balanceGhs = 428.50, // Accumulated creator balance ready to earn from
      pendingGhs = 65.00,
      totalViews = 18600,
      totalLikes = 4320,
      points = 620,
      quizzesPassed = 18
    )
    dao.insertUser(initialUser)

    // 2. Prepopulate Subjects for Primary, JHS, and SHS
    val subjects = listOf(
      // Primary
      SubjectEntity(1, "Primary", "Primary Mathematics", "MATH-PRI", "calculate", "Numbers, operations, simple fractions, and shapes for foundational numeracy.", 4),
      SubjectEntity(2, "Primary", "Science & Environment", "SCI-PRI", "science", "Exploring plants, animals, weather, and the five human senses.", 4),
      SubjectEntity(3, "Primary", "English & Reading", "ENG-PRI", "menu_book", "Grammar essentials, story comprehension, phonics, and writing skills.", 4),
      SubjectEntity(4, "Primary", "Computing Basics", "ICT-PRI", "laptop", "Parts of a computer, keyboarding, and using technology safely.", 3),

      // JHS
      SubjectEntity(5, "JHS", "Integrated Science", "SCI-JHS", "biotech", "Matter, living cells, energy conversion, ecosystems, and simple machines.", 5),
      SubjectEntity(6, "JHS", "JHS Mathematics", "MATH-JHS", "functions", "Algebraic expressions, linear equations, ratios, and plane geometry.", 5),
      SubjectEntity(7, "JHS", "Social Studies", "SOC-JHS", "public", "Our environment, Ghanaian culture, constitution, and sustainable development.", 4),
      SubjectEntity(8, "JHS", "Information Technology", "ICT-JHS", "terminal", "Computer hardware, operating systems, internet ethics, and spreadsheet basics.", 4),

      // SHS
      SubjectEntity(9, "SHS", "Core Mathematics", "MATH-SHS", "square_foot", "Quadratic equations, trigonometry, statistics, indices, and logarithms.", 6),
      SubjectEntity(10, "SHS", "Integrated Science", "SCI-SHS", "science", "Genetics, chemical bonding, electromagnetism, and environmental conservation.", 6),
      SubjectEntity(11, "SHS", "Elective Physics & Chemistry", "PHY-CHEM", "electric_bolt", "Newtonian mechanics, atomic structure, thermochemistry, and circuits.", 5),
      SubjectEntity(12, "SHS", "Elective ICT & Coding", "CODE-SHS", "code", "Algorithms, problem solving, data structures, and web fundamentals.", 5)
    )
    dao.insertSubjects(subjects)

    // 3. Prepopulate Lessons
    val lessons = listOf(
      // Primary Lessons
      LessonEntity(
        id = 1,
        subjectId = 1,
        gradeLevel = "Primary",
        classGrade = "Class 4",
        title = "Understanding Fractions and Halves",
        summary = "Learn what a fraction is, identifying numerators, denominators, and real-life examples using fruits and cakes.",
        content = """
          # What is a Fraction?
          A fraction represents a part of a whole thing. When an object is divided into equal parts, each part is a fraction of that object.
          
          ## Key Terms:
          * **Numerator**: The top number. It tells how many equal parts you have.
          * **Denominator**: The bottom number. It tells how many equal parts the whole is divided into.
          
          ### Example:
          If you cut a sweet Ghanaian pineapple into 4 equal slices and eat 1 slice:
          You ate 1/4 of the pineapple. 3/4 remains for your friends!
          
          ### Types of Fractions:
          1. Proper fractions: Numerator is smaller than denominator (e.g. 2/5).
          2. Improper fractions: Numerator is larger than or equal to denominator (e.g. 5/3).
          3. Mixed numbers: A whole number combined with a fraction (e.g. 1 1/2).
        """.trimIndent(),
        keyTakeaways = "Numerator is on top; Denominator is below. Dividing equally creates fair fractions.",
        durationMin = 10,
        difficulty = "Beginner"
      ),
      LessonEntity(
        id = 2,
        subjectId = 2,
        gradeLevel = "Primary",
        classGrade = "Class 5",
        title = "Photosynthesis: How Plants Make Food",
        summary = "Discover how green leaves trap sunlight, absorb water, and produce oxygen and glucose.",
        content = """
          # How Green Plants Feed the World
          Unlike animals, plants do not need to hunt or cook. They make their own food through a wonderful process called **Photosynthesis**!
          
          ## The 4 Key Ingredients:
          1. **Sunlight**: Captured by the green pigment named Chlorophyll.
          2. **Water (H₂O)**: Absorbed by roots from the soil.
          3. **Carbon Dioxide (CO₂)**: Taken in from the air through tiny openings in leaves called stomata.
          4. **Chlorophyll**: The green solar panels inside plant cells.
          
          ## What Plants Produce:
          * **Glucose (Sugar)**: Energy for the plant to grow tall and produce fruits.
          * **Oxygen (O₂)**: Clean, fresh air that humans and animals breathe!
        """.trimIndent(),
        keyTakeaways = "Plants need sunlight, water, carbon dioxide, and chlorophyll to produce food and oxygen.",
        durationMin = 12,
        difficulty = "Beginner"
      ),

      // JHS Lessons
      LessonEntity(
        id = 3,
        subjectId = 5,
        gradeLevel = "JHS",
        classGrade = "JHS 2",
        title = "Atomic Structure and Elements",
        summary = "Explore protons, neutrons, and electrons inside atoms, and how they define chemical elements.",
        content = """
          # Inside the Atom
          Every piece of matter in the universe is made of tiny building blocks called **atoms**.
          
          ## Subatomic Particles:
          1. **Protons**: Positively charged (+1) particles located in the central nucleus.
          2. **Neutrons**: Neutral particles (no charge) sharing the nucleus with protons.
          3. **Electrons**: Tiny negatively charged (-1) particles that orbit the nucleus in energy shells.
          
          ## Atomic Number (Z):
          The atomic number equals the number of protons in an atom. It defines which element it is!
          * Hydrogen: 1 proton
          * Carbon: 6 protons
          * Oxygen: 8 protons
          
          ## Electron Arrangement:
          Electrons occupy shells following the 2, 8, 8 rule for the first 20 elements.
        """.trimIndent(),
        keyTakeaways = "Protons (+) and neutrons (0) are in the nucleus; electrons (-) orbit in shells.",
        durationMin = 15,
        difficulty = "Standard"
      ),
      LessonEntity(
        id = 4,
        subjectId = 6,
        gradeLevel = "JHS",
        classGrade = "JHS 3",
        title = "Solving Linear Equations in One Variable",
        summary = "Master balancing equations, collecting like terms, and isolating the unknown variable x.",
        content = """
          # Linear Equations Made Simple
          Think of an equation as a two-pan balance scale. Whatever you do to one side, you must do to the other side to keep it balanced.
          
          ## Step-by-Step Method:
          1. **Expand brackets** if any exist.
          2. **Group terms**: Move all terms containing the variable (x) to the left-hand side, and constant numbers to the right-hand side.
          3. **Combine like terms**.
          4. **Divide** both sides by the coefficient of the variable.
          
          ### Walkthrough Example:
          Solve for x: `3x + 5 = 20`
          * Subtract 5 from both sides: `3x = 20 - 5` -> `3x = 15`
          * Divide both sides by 3: `x = 15 / 3` -> `x = 5`
          * Check: `3(5) + 5 = 15 + 5 = 20`. Correct!
        """.trimIndent(),
        keyTakeaways = "Keep the equation balanced: inverse operations undo addition with subtraction and multiplication with division.",
        durationMin = 14,
        difficulty = "Standard"
      ),

      // SHS Lessons
      LessonEntity(
        id = 5,
        subjectId = 9,
        gradeLevel = "SHS",
        classGrade = "SHS 2",
        title = "Quadratic Equations & The Almighty Formula",
        summary = "Solve any quadratic equation ax² + bx + c = 0 using factorisation, completing the square, or the quadratic formula.",
        content = """
          # The Quadratic Equation Formula
          A quadratic equation is in the standard form:
          `ax² + bx + c = 0` (where a ≠ 0).
          
          ## The Quadratic Formula:
          `x = [-b ± √(b² - 4ac)] / (2a)`
          
          ## The Discriminant (Δ = b² - 4ac):
          * If Δ > 0: Two distinct real roots.
          * If Δ = 0: Two equal real roots (one repeating solution).
          * If Δ < 0: Complex or imaginary roots (no real roots).
          
          ### Worked Example:
          Solve: `2x² - 4x - 6 = 0`
          * Here `a = 2`, `b = -4`, `c = -6`
          * `b² - 4ac = (-4)² - 4(2)(-6) = 16 + 48 = 64`
          * `x = [ -(-4) ± √64 ] / (2 * 2)`
          * `x = [ 4 ± 8 ] / 4`
          * `x₁ = (4 + 8) / 4 = 12 / 4 = 3`
          * `x₂ = (4 - 8) / 4 = -4 / 4 = -1`
          * Solutions are `x = 3` or `x = -1`.
        """.trimIndent(),
        keyTakeaways = "The discriminant b² - 4ac indicates the nature of roots. Use the ± operator for both solutions.",
        durationMin = 18,
        difficulty = "Advanced"
      ),
      LessonEntity(
        id = 6,
        subjectId = 12,
        gradeLevel = "SHS",
        classGrade = "SHS 3",
        title = "Algorithms, Flowcharts & Computational Thinking",
        summary = "How software engineers break complex problems into sequential, unambiguous instructions.",
        content = """
          # What is an Algorithm?
          An algorithm is an ordered, step-by-step set of instructions designed to solve a specific problem or perform a computation.
          
          ## 3 Essential Control Structures:
          1. **Sequence**: Instructions executed in strict linear order from top to bottom.
          2. **Selection (Conditionals)**: Decisions made based on conditions (e.g. `if / else`).
          3. **Iteration (Loops)**: Repeating actions until a termination condition is met (`while`, `for`).
          
          ## Flowchart Standard Symbols:
          * Oval / Rounded Rectangle: Start and End terminal.
          * Parallelogram: Input and Output operations.
          * Rectangle: Process or computation step.
          * Diamond: Decision point with Yes/No branches.
        """.trimIndent(),
        keyTakeaways = "Algorithms must be finite, precise, and unambiguous. Flowcharts visually trace logic flow.",
        durationMin = 16,
        difficulty = "Advanced"
      )
    )
    dao.insertLessons(lessons)

    // 4. Prepopulate Questions & Quizzes with Explanations & Advanced Tiers
    val questions = listOf(
      // Primary Math Questions
      QuestionEntity(
        id = 1,
        subjectId = 1,
        lessonId = 1,
        gradeLevel = "Primary",
        questionText = "In the fraction 3/8, what is the number 3 called?",
        optionA = "Denominator",
        optionB = "Numerator",
        optionC = "Quotient",
        optionD = "Divisor",
        correctOptionIndex = 1,
        explanation = "The top number of a fraction is always the Numerator. It indicates how many parts are being considered. The bottom number (8) is the Denominator.",
        isAdvanced = false
      ),
      QuestionEntity(
        id = 2,
        subjectId = 1,
        lessonId = 1,
        gradeLevel = "Primary",
        questionText = "If you have 1/2 of an orange and your friend gives you 1/4 of an orange, how much do you have in total?",
        optionA = "2/6",
        optionB = "3/4",
        optionC = "2/4",
        optionD = "1/8",
        correctOptionIndex = 1,
        explanation = "Convert 1/2 to fourths: 1/2 = 2/4. Then add 2/4 + 1/4 = 3/4.",
        isAdvanced = false
      ),
      // Advanced Primary Question
      QuestionEntity(
        id = 3,
        subjectId = 1,
        lessonId = 1,
        gradeLevel = "Primary",
        questionText = "[Advanced Challenge] A baker made 48 meat pies. He sold 3/4 of them in the morning. How many pies remain?",
        optionA = "36",
        optionB = "24",
        optionC = "12",
        optionD = "16",
        correctOptionIndex = 2,
        explanation = "Pies sold = 3/4 × 48 = 36 pies. Remaining pies = 48 - 36 = 12 pies (which is 1/4 of 48).",
        isAdvanced = true
      ),

      // JHS Science & Math Questions
      QuestionEntity(
        id = 4,
        subjectId = 5,
        lessonId = 3,
        gradeLevel = "JHS",
        questionText = "Which subatomic particle carries a negative electrical charge?",
        optionA = "Proton",
        optionB = "Neutron",
        optionC = "Electron",
        optionD = "Positron",
        correctOptionIndex = 2,
        explanation = "Electrons carry a negative electrical charge (-1) and orbit outside the nucleus.",
        isAdvanced = false
      ),
      QuestionEntity(
        id = 5,
        subjectId = 6,
        lessonId = 4,
        gradeLevel = "JHS",
        questionText = "Solve for y in the equation: 4y - 8 = 16",
        optionA = "y = 2",
        optionB = "y = 6",
        optionC = "y = 8",
        optionD = "y = 4",
        correctOptionIndex = 1,
        explanation = "Add 8 to both sides: 4y = 24. Then divide by 4: y = 24 / 4 = 6.",
        isAdvanced = false
      ),
      // Advanced JHS Challenge
      QuestionEntity(
        id = 6,
        subjectId = 6,
        lessonId = 4,
        gradeLevel = "JHS",
        questionText = "[Advanced Challenge] Solve: 2(3x - 1) = 4x + 10. What is the value of x?",
        optionA = "x = 4",
        optionB = "x = 5",
        optionC = "x = 6",
        optionD = "x = 8",
        correctOptionIndex = 2,
        explanation = "Expand: 6x - 2 = 4x + 10. Subtract 4x: 2x - 2 = 10. Add 2: 2x = 12. Divide by 2: x = 6.",
        isAdvanced = true
      ),

      // SHS Questions
      QuestionEntity(
        id = 7,
        subjectId = 9,
        lessonId = 5,
        gradeLevel = "SHS",
        questionText = "What is the discriminant of the quadratic equation x² - 6x + 9 = 0?",
        optionA = "0",
        optionB = "36",
        optionC = "-18",
        optionD = "12",
        correctOptionIndex = 0,
        explanation = "Discriminant Δ = b² - 4ac. Here a=1, b=-6, c=9. Δ = (-6)² - 4(1)(9) = 36 - 36 = 0. This means the equation has one repeated real root (x = 3).",
        isAdvanced = false
      ),
      QuestionEntity(
        id = 8,
        subjectId = 12,
        lessonId = 6,
        gradeLevel = "SHS",
        questionText = "In a standard flowchart, which geometric shape represents a conditional decision point?",
        optionA = "Rectangle",
        optionB = "Diamond",
        optionC = "Parallelogram",
        optionD = "Oval",
        correctOptionIndex = 1,
        explanation = "A Diamond represents a Decision (e.g. Yes/No, True/False) leading to multiple alternative paths.",
        isAdvanced = false
      ),
      // Advanced SHS Question
      QuestionEntity(
        id = 9,
        subjectId = 9,
        lessonId = 5,
        gradeLevel = "SHS",
        questionText = "[Advanced Challenge] If the roots of 2x² - kx + 8 = 0 are real and equal, what are the possible values of k?",
        optionA = "k = ±4",
        optionB = "k = ±8",
        optionC = "k = ±16",
        optionD = "k = ±64",
        correctOptionIndex = 1,
        explanation = "For real and equal roots, Δ = b² - 4ac = 0. Here (-k)² - 4(2)(8) = 0 -> k² - 64 = 0 -> k² = 64 -> k = ±8.",
        isAdvanced = true
      )
    )
    dao.insertQuestions(questions)

    // 5. Prepopulate Chat Groups
    val groups = listOf(
      ChatGroupEntity(1, "FTY JHS General Hub", "Study circle for JHS candidates preparing for BECE", "JHS", 128, "school"),
      ChatGroupEntity(2, "SHS Science & Math Olympiad", "Advanced problem-solving in Physics, Chemistry & Core Maths", "SHS", 240, "biotech"),
      ChatGroupEntity(3, "Primary Kids Explorers", "Friendly, supervised learning chat for young learners", "Primary", 64, "child_care"),
      ChatGroupEntity(4, "Faith Yenpang Official Channel", "Direct updates, study tips, and announcements from Tembilla Faith Yenpang", "All", 512, "campaign"),
      ChatGroupEntity(5, "Ghana ICT & Digital Skills Hub", "Coding, digital entrepreneurship, internet safety, and AI basics", "All", 185, "devices")
    )
    dao.insertChatGroups(groups)

    // 6. Prepopulate Messages
    val messages = listOf(
      ChatMessageEntity(
        id = 1,
        groupId = 4,
        senderId = 99,
        senderName = "Faith Yenpang (Creator)",
        senderAvatar = "",
        text = "Welcome to FTY! Education is the greatest bridge to opportunity. Make sure to try our daily quizzes and take advantage of our affordable data bundles!",
        timestamp = System.currentTimeMillis() - 86400000,
        isPrivate = false
      ),
      ChatMessageEntity(
        id = 2,
        groupId = 1,
        senderId = 2,
        senderName = "Kwame Mensah",
        senderAvatar = "",
        text = "Hello everyone! Who has tried the new Integrated Science quiz on Atomic Structure? The explanation for question 4 helped me a lot.",
        timestamp = System.currentTimeMillis() - 3600000,
        isPrivate = false
      ),
      ChatMessageEntity(
        id = 3,
        groupId = 1,
        senderId = 3,
        senderName = "Abena Osei",
        senderAvatar = "",
        text = "Yes Kwame! Remember the electrons orbit in shells 2, 8, 8. Very easy once you practice!",
        timestamp = System.currentTimeMillis() - 1800000,
        isPrivate = false
      ),
      // Sample 1-on-1 private message
      ChatMessageEntity(
        id = 4,
        groupId = null,
        senderId = 99,
        senderName = "Faith Yenpang (Creator)",
        senderAvatar = "",
        recipientId = 1,
        text = "Hello Faith! Keep up the brilliant quiz scores. Remember consistency is the key to mastering both science and programming.",
        timestamp = System.currentTimeMillis() - 7200000,
        isPrivate = true
      )
    )
    dao.insertMessages(messages)

    // 7. Prepopulate Short Videos
    val videos = listOf(
      VideoPostEntity(
        id = 1,
        creatorId = 1,
        creatorName = "Faith Yenpang",
        creatorAvatar = "",
        title = "Solve Any Quadratic Equation in 60 Seconds ⚡",
        description = "Quick mental trick to calculate the discriminant and identify roots without panicking in the exam room!",
        category = "Mathematics",
        durationSec = 58,
        likesCount = 1420,
        commentsCount = 89,
        viewsCount = 7800,
        isLikedByMe = true,
        isMonetized = true,
        earnedGhs = 78.00,
        createdAt = System.currentTimeMillis() - 259200000
      ),
      VideoPostEntity(
        id = 2,
        creatorId = 1,
        creatorName = "Faith Yenpang",
        creatorAvatar = "",
        title = "Why Fast Affordable Data Empowers Students in Ghana 🇬🇭",
        description = "Never run out of bundles during online research. Check our FTY Fast Data tab for instant bundles on MTN & Telecel!",
        category = "Community",
        durationSec = 45,
        likesCount = 2150,
        commentsCount = 134,
        viewsCount = 12400,
        isLikedByMe = false,
        isMonetized = true,
        earnedGhs = 124.00,
        createdAt = System.currentTimeMillis() - 172800000
      ),
      VideoPostEntity(
        id = 3,
        creatorId = 1,
        creatorName = "Faith Yenpang",
        creatorAvatar = "",
        title = "How the Human Heart Pumps Blood: Visualized ❤️",
        description = "Right atrium to ventricle, pulmonary artery to the lungs, then oxygenated blood to the body! JHS & SHS biology simplified.",
        category = "Science",
        durationSec = 52,
        likesCount = 980,
        commentsCount = 42,
        viewsCount = 5600,
        isLikedByMe = false,
        isMonetized = true,
        earnedGhs = 56.00,
        createdAt = System.currentTimeMillis() - 86400000
      ),
      VideoPostEntity(
        id = 4,
        creatorId = 1,
        creatorName = "Faith Yenpang",
        creatorAvatar = "",
        title = "Writing Your First Python Function in 30 Seconds 💻",
        description = "def greet(name): return f'Hello {name}'. Let's build real coding skills for our Ghanaian youth!",
        category = "Education",
        durationSec = 35,
        likesCount = 1890,
        commentsCount = 98,
        viewsCount = 9200,
        isLikedByMe = false,
        isMonetized = true,
        earnedGhs = 92.00,
        createdAt = System.currentTimeMillis() - 43200000
      )
    )
    dao.insertVideos(videos)

    // Prepopulate comments for video 1
    val comments = listOf(
      VideoCommentEntity(1, 1, "Ebenezer Quaye", "", "This trick saved me during my mock exam yesterday! Thank you FTY!", System.currentTimeMillis() - 72000000),
      VideoCommentEntity(2, 1, "Grace Asante", "", "Clear explanation, straight to the point. More math shorts please!", System.currentTimeMillis() - 36000000),
      VideoCommentEntity(3, 2, "Kofi Boadu", "", "The FTY Fast Data bundle really delivers fast. Very reliable for my night studies.", System.currentTimeMillis() - 18000000)
    )
    dao.insertVideoComments(comments)

    // Prepopulate past payout request in creator dashboard
    val payout = PayoutRequestEntity(
      id = 1,
      creatorId = 1,
      amountGhs = 150.00,
      provider = "MTN Mobile Money",
      momoNumber = "0244123456",
      accountName = "Faith Yenpang",
      status = "COMPLETED",
      requestedAt = System.currentTimeMillis() - 604800000
    )
    dao.insertPayoutRequest(payout)
  }
}
