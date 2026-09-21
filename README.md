# 📱 Digital ID --- Mobile Application Development

> **Project:** Digital Identity Management System for Students  
> **Platform:** Android  
> **Language:** Kotlin  
> **Database:** SQLite

---

## 🎯 Objective

This project demonstrates the development of an Android-based **Digital ID Card Management System**.

The application allows students to create accounts and view their Digital ID cards, while an administrator can manage student records and assign or update official Digital ID information.

The project demonstrates Android UI development, Activities, Explicit Intent, RecyclerView, Adapter, SQLite database operations, image selection, internal storage, and dynamic data display.

---

## 🧩 Application Features

The application contains two main user roles:

- **Student**
- **Admin**

### 👨‍🎓 Student Features

A student can:

- Create an account.
- Log in using email and password.
- View the Student Dashboard.
- View the assigned Digital ID.
- View personal Digital ID information.
- Log out of the application.

### 👨‍💼 Admin Features

An administrator can:

- Log in using an Admin account.
- Open the Admin Dashboard.
- View registered students.
- Check Digital ID assignment status.
- Select a student.
- Assign a Digital ID.
- Update an existing Digital ID.
- Upload or update the student's photograph.

> **Note:** The Role field is temporarily used during development to create the first Admin account. It can be removed from normal student registration after the Admin account has been created.

---

## 🔄 Application Flow

```text
                         ┌───────────────┐
                         │ Splash Screen │
                         └───────┬───────┘
                                 │
                                 ▼
                         ┌───────────────┐
                         │  Main Screen  │
                         └───────┬───────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
                    ▼                         ▼
              ┌──────────┐              ┌──────────┐
              │  Sign Up │              │  Login   │
              └────┬─────┘              └────┬─────┘
                   │                         │
                   ▼                         ▼
            Create Account             Verify SQLite
                                             │
                                    ┌────────┴────────┐
                                    │                 │
                                    ▼                 ▼
                              ┌───────────┐    ┌────────────┐
                              │  Student  │    │   Admin    │
                              │ Dashboard │    │ Dashboard  │
                              └─────┬─────┘    └─────┬──────┘
                                    │                 │
                                    ▼                 ▼
                              View Digital ID   Student Management
                                                      │
                                                      ▼
                                                Select Student
                                                      │
                                                      ▼
                                               Add / Update ID
                                                      │
                                                      ▼
                                                    SQLite
```

---

## 🖥️ Main Screens

### 1. Splash Screen

The Splash Screen displays:

- Digital ID logo
- Application name
- Digital Identity Management System

---

### 2. Main Screen

The Main Screen provides navigation to:

- **Login**
- **Sign Up**

---

### 3. Signup Screen

The Signup screen collects:

| Field | Purpose |
|---|---|
| Full Name | Student's name |
| Email | University email |
| Phone | Student mobile number |
| Batch Year | Academic batch |
| Password | Account password |
| Confirm Password | Password confirmation |
| Role | Temporary Admin/Student role field |

Basic validation is performed before creating the account.

---

### 4. Login Screen

The Login screen contains:

- Email
- Password
- Login button
- Sign Up navigation

The entered credentials are checked against the SQLite `users` table.

After successful authentication, the application checks the stored role and opens the corresponding dashboard.

---

### 5. Student Dashboard

The Student Dashboard displays:

- Student name
- Digital ID section
- View Digital ID button
- Logout button

The student's name is retrieved dynamically from SQLite.

---

### 6. Admin Dashboard

The Admin Dashboard provides:

- Student Management
- Manage Student Digital IDs
- Logout

---

### 7. Student Management

The Admin can view students using a **RecyclerView**.

The student list contains:

| Name | Enrollment No. | Batch | ID Status |
|---|---|---|---|

Selecting a student opens the Digital ID management screen.

---

### 8. Add / Update Digital ID

The Admin can enter or update:

- Enrollment Number
- College
- Degree
- Blood Group
- Validity
- Student Photograph

If the student already has a Digital ID, the existing information is loaded and can be updated.

---

### 9. Digital ID Display

The Digital ID is displayed dynamically using information stored in SQLite.

The card contains:

- Student Name
- Student Photograph
- Enrollment Number
- College
- Degree
- Blood Group
- Mobile Number
- Validity

The layout is designed to represent the university student ID card format.

---

## 🗄️ Database

The application uses **SQLite** for local data storage.

### `users` Table

Stores account and basic student information.

| Field | Description |
|---|---|
| `id` | Unique user ID |
| `email` | User email |
| `phone` | Mobile number |
| `fullName` | User name |
| `batch` | Academic batch |
| `password` | Account password |
| `role` | Student/Admin role |

### `digital_id` Table

Stores official Digital ID information.

| Field | Description |
|---|---|
| `userId` | Related user ID |
| `enrollmentNo` | Enrollment number |
| `college` | College name |
| `degree` | Degree/program |
| `bGroup` | Blood group |
| `validity` | ID validity |
| `photo` | Stored photograph path |

### Database Relationship

```text
users
  │
  │  userId
  ▼
digital_id
```

The `userId` associates a Digital ID record with its corresponding user.

---

## 🖼️ Student Photograph

The Admin selects the student's photograph using the Android image picker.

The selected image is:

1. Selected from the device.
2. Displayed in the ImageView.
3. Copied into the application's internal storage.
4. Its file path is stored in SQLite.
5. The stored path is used later to display the photograph.

This allows the Digital ID to display the student's actual photograph instead of using a fixed image.

---

## 🧱 Project Structure

```text
Digital ID
│
├── SplashActivity
├── MainActivity
├── LoginActivity
├── SignupActivity
│
├── StudentDashboardActivity
├── DigitalIDActivity
│
├── AdminDashboardActivity
├── StudentListActivity
├── AddDigitalIDActivity
│
├── DatabaseHelper
├── User
├── DigitalID
├── StudentListItem
└── StudentAdapter
```

---

## 📁 Layout Files

```text
res/layout/
│
├── activity_splash.xml
├── activity_main.xml
├── activity_login.xml
├── activity_signup.xml
├── activity_student_dashboard.xml
├── activity_digital_idactivity.xml
├── activity_admin_dashboard.xml
├── activity_student_list.xml
├── activity_add_digital_id.xml
└── item_student.xml
```

---

## 🛠️ Technologies Used

- **Android Studio**
- **Kotlin**
- **XML**
- **SQLite**
- **ConstraintLayout**
- **MaterialCardView**
- **TextInputLayout**
- **RecyclerView**
- **Adapter**
- **TableLayout**
- **Explicit Intent**
- **Activity**
- **Android Image Picker**
- **Internal Storage**

---

## 📚 MAD Concepts Demonstrated

The project demonstrates the following Mobile Application Development concepts:

- Android Activities
- Activity Life Cycle
- XML-based UI Design
- ConstraintLayout
- MaterialCardView
- TextInputLayout
- TextInputEditText
- Buttons
- TextViews
- ImageView
- TableLayout and TableRow
- RecyclerView
- Adapter
- Explicit Intent
- SQLite Database
- Database Helper
- Insert and Retrieve operations
- Update operations
- Data validation
- Image selection
- Internal application storage
- Dynamic data display
- Role-based navigation

---

## ✅ Validation

The application performs basic validation such as:

- Checking that required fields are filled.
- Checking whether an email is already registered.
- Checking password and confirm password.
- Checking whether a Digital ID already exists.
- Inserting a new Digital ID when no record exists.
- Updating an existing Digital ID when a record already exists.
- Checking that a student photograph has been selected before saving the Digital ID.

---

## 📁 Updated / Added Files

### Kotlin Files

```text
MainActivity.kt
SplashActivity.kt
LoginActivity.kt
SignupActivity.kt
StudentDashboardActivity.kt
DigitalIDActivity.kt
AdminDashboardActivity.kt
StudentListActivity.kt
AddDigitalIDActivity.kt
DatabaseHelper.kt
User.kt
DigitalID.kt
StudentListItem.kt
StudentAdapter.kt
```

### XML Files

```text
activity_main.xml
activity_splash.xml
activity_login.xml
activity_signup.xml
activity_student_dashboard.xml
activity_digital_idactivity.xml
activity_admin_dashboard.xml
activity_student_list.xml
activity_add_digital_id.xml
item_student.xml
```

---

## ▶️ How to Run

1. Open the project in **Android Studio**.
2. Allow Gradle synchronization to complete.
3. Build the project.
4. Run the application on an emulator or Android device.
5. Open the application through the Splash Screen.
6. Create an account using the Signup screen.
7. Log in using the registered credentials.
8. Use the Student or Admin features according to the stored role.
9. For Admin, open Student Management and select a student.
10. Assign or update the student's Digital ID.
11. Log in as the student and open **View Digital ID**.

---

## 🖼️ OUTPUT

Add screenshots of the following screens here:

```text
1. Splash Screen
2. Main Screen
3. Signup Screen
4. Login Screen
5. Student Dashboard
6. Admin Dashboard
7. Student Management
8. Add / Update Digital ID
9. Digital ID Card
```

Example:

```text
[ Screenshot – Splash Screen ]

[ Screenshot – Login Screen ]

[ Screenshot – Student Dashboard ]

[ Screenshot – Admin Dashboard ]

[ Screenshot – Student Management ]

[ Screenshot – Digital ID Card ]
```

---

## 📝 Result

The **Digital ID – Mobile Application Development** project was successfully developed as an Android application.

The application provides separate Student and Admin functionality, stores user and Digital ID information using SQLite, manages student records using RecyclerView and Adapter, stores student photographs in application storage, and dynamically displays the Digital ID card using the stored information.

---

## 👨‍💻 Author

**Vraj Soni**

**B.Tech Computer Engineering**  
**Ganpat University**
