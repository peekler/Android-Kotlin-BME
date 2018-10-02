package hu.aut.android.kotlinroomdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import hu.aut.android.kotlinroomdemo.R.id.etGrade
import hu.aut.android.kotlinroomdemo.R.id.etStudentId
import hu.aut.android.kotlinroomdemo.R.id.tvResult
import hu.aut.android.kotlinroomdemo.data.AppDatabase
import hu.aut.android.kotlinroomdemo.data.Grade


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSubmit.setOnClickListener {
            val grade = Grade(null, etStudentId.text.toString(),
                    etGrade.text.toString())

            val dbThread = Thread {
                AppDatabase.getInstance(this@MainActivity).gradeDao().insertGrades(grade)
            }
            dbThread.start()
        }

        btnSearch.setOnClickListener {
            val dbThread = Thread {
                    val grades = AppDatabase.getInstance(this@MainActivity).gradeDao()
                            .getSpecificGrades("A+")
                    runOnUiThread {
                        tvResult.text = ""
                        grades.forEach {
                            tvResult.append("${it.studentId} ${it.grade}\n")
                        }
                    }
            }
            dbThread.start()
        }
    }
}
