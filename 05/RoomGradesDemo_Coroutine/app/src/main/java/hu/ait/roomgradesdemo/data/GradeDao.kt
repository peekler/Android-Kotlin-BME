package hu.ait.roomgradesdemo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GradeDAO {
    @Query("""SELECT * FROM grade WHERE grade="B"""")
    suspend fun getBGrades(): List<Grade>

    @Query("SELECT * FROM grade WHERE grade = :grade")
    suspend fun getSpecificGrades(grade: String): List<Grade>

    @Query("SELECT * FROM grade")
    suspend fun getAllGrades(): List<Grade>

    @Insert
    suspend fun insertGrades(vararg grades: Grade)

    @Delete
    suspend fun deleteGrade(grade: Grade)

    @Query("DELETE FROM grade")
    suspend fun deleteAllGrades()
}
