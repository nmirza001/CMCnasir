# Choose My College (CMC)

## CSCI 230: Software Development - Spring 2025
College of Saint Benedict / Saint John's University

---

## 📄 Project Overview
Choose My College (CMC) is a Java console application developed as the final project for CSCI 230. It allows users to explore and manage university data. Regular users can search and save schools, while administrators can manage users and update university records. The application follows clean separation between frontend and backend logic and emphasizes test-driven development and extensibility.

---

## 👨‍💻 Project Team
- Nasir Mirza
- Rick Masaana
- Alex
- Roman Lefler
- Timmy

---

## 🎯 Core Features

### 🔓 Login System
- Secure login with username and password
- Active/inactive status for account control

### 🧑‍🎓 For Regular Users
- **Search** universities by state (case-insensitive), student count, or both
- **Empty search** returns all universities
- **View full university profiles** with location, control, SATs, admissions, and emphasis
- **Save universities** for later
- **View saved schools** and their details
- **Find similar schools** using a multi-criteria algorithm

### 🛠️ For Admins
- **Manage Users**: Add, deactivate/reactivate, delete, search by username
- **Manage Universities**:
  - Add new universities
  - Edit full profile including emphasis, image URL, and webpage
  - Remove schools from the system

---

## 🧠 Architecture
```
CMC/
├── src/
│   └── cmc/
│       ├── backend/
│       │   ├── controllers/         # Business logic
│       │   └── entities/            # Models (University, User)
│       ├── frontend/                # UI interfaces and menus
│       └── CMCException.java        # Custom error handling
├── test/
│   ├── backend/                     # Unit tests
│   ├── regression/                  # Regression bug checks
│   └── userstory/                   # Tests for user-centered features
└── lib/                             # External libraries (JUnit, DB Library)
```

---

## 📊 Similarity Matching Logic
The `findSimilar()` method returns universities that match at least **3 out of 7** criteria:
- State
- Location (urban/suburban/rural)
- Control (public/private)
- Student body size (±25%)
- SAT score range (±75 points)
- Acceptance rate (±15%)
- Academic scale (±1)

---

## ⚙️ Setup Instructions

### 📥 Clone the Repository
Clone into your Eclipse workspace or preferred IDE.

### 🛠 Requirements
- Java JDK 1.8
- Eclipse or IntelliJ (or CLI)
- JUnit 4
- UniversityDBLibrary.jar (if connecting to real DB)

---

## 🧪 Running the Program

### ✅ In Eclipse
1. Import the project
2. Right-click `Driver.java` in `src/cmc/frontend/`
3. Select **Run As > Java Application**

### 🧪 Running Tests in Eclipse
1. Navigate to `test/`
2. Right-click `AllTests.java` or any test file
3. Select **Run As > JUnit Test**

### 🖥️ Command Line
#### Compile
```bash
javac -d bin -cp "lib/*" src/cmc/**/*.java test/cmc/**/*.java
```
#### Run App
```bash
java -cp "bin:lib/*" cmc.frontend.Driver
```
#### Run Tests
```bash
java -cp "bin:lib/*" org.junit.runner.JUnitCore cmc.AllTests
```

---

## 🧪 Testing Philosophy
- Test-driven structure using JUnit 4
- Extensive unit, regression, and user scenario tests
- Supports testing with or without a real database (via MockDatabaseController)

---

## 🎓 Academic Use
This application was created for educational use as part of a software development course. All code is student-written and demonstrates principles of modularity, MVC separation, test coverage, and user-centered design.




