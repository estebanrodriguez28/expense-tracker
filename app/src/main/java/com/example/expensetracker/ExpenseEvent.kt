package com.example.expensetracker

sealed interface ExpenseEvent {
    data class SortContacts(val sortType: SortType) : ExpenseEvent
}