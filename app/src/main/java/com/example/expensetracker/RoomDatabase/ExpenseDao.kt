package com.example.expensetracker.RoomDatabase

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExpenses(entities: List<ExpenseEntity>)

    @Query("SELECT * FROM expenses WHERE id = :expenseID")
    suspend fun getExpenseID(expenseID : Long) : ExpenseEntity?

    @Update
    suspend fun updateExpense(expense: ExpenseEntity) : Int

    @Update
    suspend fun updateAllExpenses(expensesList: List<ExpenseEntity>)

    @Query("SELECT * FROM expenses ORDER BY title ASC")
    fun sortExpenseByTitle() : Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY amount DESC")
    fun sortExpenseByAmount() : Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY category ASC")
    fun sortExpenseByCategory() : Flow<List<ExpenseEntity>>



    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

}