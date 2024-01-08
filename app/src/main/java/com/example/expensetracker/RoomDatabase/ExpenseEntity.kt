package com.example.expensetracker.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expenses")

data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var title: String,
    var amount: Float,
    var category: String

) : Serializable
