<<<<<<< HEAD
# CodeAlpha_StudentGradeTracker

> **CodeAlpha Java Programming Internship — Task 1: Student Grade Tracker**

A feature-rich, object-oriented Java application to manage student grade records, compute statistical analytics (Average, Highest, Lowest, Letter Grades, and GPA), and generate summary reports. Includes both a **Modern Swing Desktop GUI** and an **Interactive Terminal CLI** interface.

---

## 📌 Features

- **Student & Grade Management**:
  - Add, edit, and remove student records using dynamic `ArrayList` data structures.
  - Store individual exam and assignment scores (0-100 scale).
  - Pre-loaded with realistic sample dataset for quick testing.

- **Statistical Metrics**:
  - **Individual**: Average score, Highest score, Lowest score, Letter Grade (A/B/C/D/F), and GPA (4.0 scale).
  - **Class Aggregate**: Overall class average, highest score, lowest score, top student performer, and grade distribution histogram.

- **User Interfaces**:
  - **Modern Swing Desktop GUI**: Clean layout with metric summary cards, styled data tables, action modals, and file export options.
  - **Console CLI Mode**: ASCII table rendering for running directly inside terminal/VS Code console.

- **Export & File Persistence**:
  - Export full class grade table to **CSV** format (`student_grades.csv`).
  - Export clean ASCII summary report to **TXT** file (`grade_summary_report.txt`).

---

## 📁 Repository Structure

```
CodeAlpha_StudentGradeTracker/
├── .vscode/
│   ├── launch.json              # VS Code Debug & 1-click execution configs
│   └── settings.json            # VS Code Java classpath configuration
├── src/
│   └── com/codealpha/gradetracker/
│       ├── Main.java            # Main entry point (GUI / Console launcher)
│       ├── model/
│       │   ├── Student.java     # OOP Student model with metric calculations
│       │   └── GradeSummary.java# DTO for aggregate class metrics
│       ├── service/
│       │   └── GradeTrackerService.java # Business logic, CRUD & persistence
│       └── ui/
│           ├── StudentGradeTrackerGUI.java  # Modern Swing Desktop Window
│           └── StudentGradeTrackerConsole.java# Interactive Terminal CLI
├── compile_and_run.bat          # 1-Click batch script to compile & run GUI
├── run_console.bat              # 1-Click batch script to run Terminal CLI
└── README.md                    # Project documentation
```

---

## 🚀 How to Run in VS Code

### Option 1: VS Code Native Runner (Recommended)
1. Open this directory in **Visual Studio Code**:
   `File -> Open Folder... -> CodeAlpha_StudentGradeTracker`
2. Open `src/com/codealpha/gradetracker/Main.java`.
3. Press **F5** (or click **Run Without Debugging**).
4. Select **"Run Student Grade Tracker (GUI)"** or **"Run Student Grade Tracker (Console CLI)"**.

### Option 2: Command Line (Windows / Terminal)
```bash
# Compile the project
javac -d bin -sourcepath src src/com/codealpha/gradetracker/Main.java

# Run Desktop GUI
java -cp bin com.codealpha.gradetracker.Main

# Run Terminal CLI Mode
java -cp bin com.codealpha.gradetracker.Main --console
```

### Option 3: Double-Click Batch Files
- Double-click `compile_and_run.bat` to launch the Desktop GUI.
- Double-click `run_console.bat` to run inside the terminal.

---

## ⚙️ Requirements
- **Java SE Development Kit (JDK)**: Java 8 or higher (Tested on JDK 25).
- **IDE**: Visual Studio Code (with *Extension Pack for Java*) or Intellij IDEA / Eclipse.
=======
# Codealpha_task1_studentgradetracker
>>>>>>> 1728c7aa89e151af6d32883bb8d47ecec923a4a1
