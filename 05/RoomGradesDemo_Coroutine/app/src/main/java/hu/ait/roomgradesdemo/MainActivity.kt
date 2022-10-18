package hu.ait.roomgradesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import hu.ait.roomgradesdemo.data.AppDatabase
import hu.ait.roomgradesdemo.data.Grade
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave.setOnClickListener {
            var newGrade = Grade(
                null,
                etName.text.toString(),
                etGrade.text.toString()
            )


            lifecycleScope.launch(Dispatchers.IO) {
                saveGrade(newGrade)
            }

        }

        btnQuery.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val list: List<Grade> =
                    lifecycleScope.async(Dispatchers.IO) {
                        queryGrades()
                    }.await()

                tvResult.text = ""
                list.forEach {
                    tvResult.append("Name: ${it.studentName} grade: ${it.grade}\n")
                }
            }
        }

        btnDeleteAll.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                deleteAllGrades()
            }
        }

    }

    suspend fun saveGrade(grade: Grade) {
        AppDatabase.getInstance(this@MainActivity).gradeDao().
        insertGrades(grade)
    }

    suspend fun queryGrades(): List<Grade> {
        //Thread.sleep(5000)
        return AppDatabase.getInstance(this@MainActivity).gradeDao().getAllGrades()
    }

    suspend fun deleteAllGrades() {
        AppDatabase.getInstance(this@MainActivity).gradeDao().deleteAllGrades()
    }

}
