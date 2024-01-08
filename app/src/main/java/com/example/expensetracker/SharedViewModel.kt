package com.example.expensetracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.expensetracker.RoomDatabase.ExpenseEntity

class SharedViewModel : ViewModel() {
    val selectedExpense = mutableStateOf<ExpenseEntity?>(null)
}