package com.example.expensetracker

import com.example.expensetracker.RoomDatabase.ExpenseEntity

data class ExpensesState(
    val expenses: List<ExpenseEntity> = emptyList(),
    val sortType: SortType = SortType.TITLE
)
