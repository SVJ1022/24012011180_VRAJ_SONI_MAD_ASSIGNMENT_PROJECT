package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $TABLE_USER (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_EMAIL TEXT," +
                "$KEY_PHONE TEXT," +
                "$KEY_FULL_NAME TEXT," +
                "$KEY_BATCH TEXT," +
                "$KEY_PASSWORD TEXT," +
                "$KEY_ROLE TEXT" +
                ");"

        db!!.execSQL(createUserTable)

        val createDigitalIDTable = "CREATE TABLE $TABLE_DIGITAL_ID(" +
                "$KEY_USER_ID INTEGER PRIMARY KEY," +
                "$KEY_ENROLLMENT_NO TEXT," +
                "$KEY_COLLEGE TEXT," +
                "$KEY_DEGREE TEXT," +
                "$KEY_BGROUP TEXT," +
                "$KEY_VALIDITY TEXT" +
                "$KEY_PHOTO TEXT" +
                ");"

        db.execSQL(createDigitalIDTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val dropUserTable = "DROP TABLE IF EXISTS $TABLE_USER"
        db!!.execSQL(dropUserTable)

        val dropDigitalIDTable = "DROP TABLE IF EXISTS $TABLE_DIGITAL_ID"
        db.execSQL(dropDigitalIDTable)

        onCreate(db)
    }

    companion object{
        private val DB_NAME = "DigitalIDDB.db"
        private val DB_VERSION = 1
        private val TABLE_USER = "users"
        private val KEY_ID = "id"
        private val KEY_EMAIL = "email"
        private val KEY_PHONE = "phone"
        private val KEY_FULL_NAME = "fullName"
        private val KEY_BATCH = "batch"
        private val KEY_PASSWORD = "password"
        private val KEY_ROLE = "role"

        private val TABLE_DIGITAL_ID = "digital_id"
        private val KEY_USER_ID = "userId"
        private val KEY_ENROLLMENT_NO = "enrollmentNo"
        private val KEY_COLLEGE = "college"
        private val KEY_DEGREE = "degree"
        private val KEY_BGROUP = "bGroup"
        private val KEY_PHOTO = "photo"
        private val KEY_VALIDITY = "validity"
    }

//    User manipulation Functions

    fun insertUser(user: User){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_EMAIL, user.email)
        values.put(KEY_PHONE, user.phone)
        values.put(KEY_FULL_NAME, user.fullName)
        values.put(KEY_BATCH, user.batch)
        values.put(KEY_PASSWORD, user.password)
        values.put(KEY_ROLE, user.role)

        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun checkUser(email:String):Boolean{
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_USER,
            arrayOf(KEY_ID),
            "$KEY_EMAIL=?",
            arrayOf(email),
            null,
            null,
            null
        )
        val exists = cursor.count > 0

        cursor.close()
        db.close()

        return exists
    }

    fun getUser(email: String,password: String): User?{
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_USER,
            arrayOf(
                KEY_ID,
                KEY_EMAIL,
                KEY_PHONE,
                KEY_FULL_NAME,
                KEY_BATCH,
                KEY_PASSWORD,
                KEY_ROLE
            ),
            "$KEY_EMAIL=? AND $KEY_PASSWORD=?",
            arrayOf(email,password),
            null,
            null,
            null
        )

        if(cursor.moveToFirst()){
            val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_FULL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_BATCH)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE))
            )
            cursor.close()
            db.close()

            return user
        }

        cursor.close()
        db.close()

        return null
    }

    fun deleteUser(user: User){
        val db = this.writableDatabase
        db.delete( TABLE_USER,
            "$KEY_ID=?",
            arrayOf(user.Id.toString())
        )
        db.close()
    }

//    Digital ID Functions

    fun getDigitalID(userId:Int): DigitalID?{
        val db = readableDatabase

        val cursor = db.query(
            TABLE_DIGITAL_ID,
            arrayOf(
                KEY_USER_ID,
                KEY_ENROLLMENT_NO,
                KEY_COLLEGE,
                KEY_DEGREE,
                KEY_BGROUP,
                KEY_VALIDITY,
                KEY_PHOTO
            ),
            "$KEY_USER_ID=?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {

            val digitalID = DigitalID(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ENROLLMENT_NO)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_COLLEGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEGREE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_BGROUP)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALIDITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO))
            )

            cursor.close()
            db.close()

            return digitalID
        }

        cursor.close()
        db.close()

        return null
    }

    fun checkDigitalID(userId: Int): Boolean {
        val db = readableDatabase

        val cursor = db.query(
            TABLE_DIGITAL_ID,
            arrayOf(KEY_USER_ID),
            "$KEY_USER_ID=?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        val exists = cursor.count > 0

        cursor.close()
        db.close()

        return exists
    }

    fun insertDigitalId(
        userId: Int,
        enrollmentNo: String,
        college: String,
        degree: String,
        bGroup: String,
        validity: String,
        photo: String
    ){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_USER_ID, userId)
        values.put(KEY_ENROLLMENT_NO, enrollmentNo)
        values.put(KEY_COLLEGE, college)
        values.put(KEY_DEGREE, degree)
        values.put(KEY_BGROUP, bGroup)
        values.put(KEY_VALIDITY, validity)
        values.put(KEY_PHOTO, photo)

        db.insert(TABLE_DIGITAL_ID,null,values)
        db.close()
    }

    fun getAllStudents(): Array<StudentListItem> {

        val studentList = ArrayList<StudentListItem>()

        val db = readableDatabase

        val query = """
        SELECT 
            users.id,
            users.fullName,
            users.batch,
            digital_id.enrollmentNo,
            CASE
                WHEN digital_id.userId IS NULL THEN 'Not Assigned'
                ELSE 'Assigned'
            END AS idStatus
        FROM users
        LEFT JOIN digital_id
        ON users.id = digital_id.userId
        WHERE users.role = ?
    """.trimIndent()

        val cursor = db.rawQuery(
            query,
            arrayOf("STUDENT")
        )

        while (cursor.moveToNext()) {

            val student = StudentListItem(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                cursor.getString(cursor.getColumnIndexOrThrow("batch")),
                cursor.getString(
                    cursor.getColumnIndexOrThrow("enrollmentNo")
                ) ?: "-",
                cursor.getString(
                    cursor.getColumnIndexOrThrow("idStatus")
                )
            )

            studentList.add(student)
        }

        cursor.close()
        db.close()

        return studentList.toTypedArray()
    }

    fun getUserById(userId: Int): User? {
        val db = readableDatabase

        val cursor = db.query(
            TABLE_USER,
            arrayOf(
                KEY_ID,
                KEY_EMAIL,
                KEY_PHONE,
                KEY_FULL_NAME,
                KEY_BATCH,
                KEY_PASSWORD,
                KEY_ROLE
            ),
            "$KEY_ID=?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {

            val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_FULL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_BATCH)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE))
            )

            cursor.close()
            db.close()

            return user
        }

        cursor.close()
        db.close()

        return null
    }

    fun updateDigitalId(
        userId:Int,
        enrollmentNo: String,
        college: String,
        degree: String,
        bGroup: String,
        validity: String,
        photo:String
    ){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_ENROLLMENT_NO, enrollmentNo)
        values.put(KEY_COLLEGE, college)
        values.put(KEY_DEGREE, degree)
        values.put(KEY_BGROUP, bGroup)
        values.put(KEY_VALIDITY, validity)
        values.put(KEY_PHOTO, photo)

        db.update(
            TABLE_DIGITAL_ID,
            values,
            "$KEY_USER_ID=?",
            arrayOf(userId.toString())
        )

        db.close()
    }

}