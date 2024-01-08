package com.example.expensetracker.ui.theme

sealed class Screen (val route: String) {
    object ExpenseHome: Screen("ExpenseHome")
    object AddExpense: Screen("AddExpense")
    object UpdateExpense: Screen("UpdateExpense")


    fun withArgs(vararg args: String?): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}
