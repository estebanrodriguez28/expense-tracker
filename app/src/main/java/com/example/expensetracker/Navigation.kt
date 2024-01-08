package com.example.expensetracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensetracker.RoomDatabase.ExpenseViewModel
import com.example.expensetracker.ui.theme.Screen


@Composable
fun Navigation(
    expenseViewModel : ExpenseViewModel,
    state: ExpensesState,
    onEvent: (ExpenseEvent) -> Unit
) {

    val navController = rememberNavController()
    val sharedViewModel = remember { SharedViewModel() }

    NavHost(navController = navController, startDestination = Screen.ExpenseHome.route) {
        composable(Screen.ExpenseHome.route) {
            ExpenseListScreen(
                navController = navController,
                expenseViewModel = expenseViewModel,
                sharedViewModel = sharedViewModel,
                state = state,
                onEvent = onEvent
            )
        }

        composable(Screen.AddExpense.route) {
            ExpenseAddScreen(
                navController = navController,
                expenseViewModel = expenseViewModel
            )
        }

        composable(
            Screen.UpdateExpense.route + "/{expenseTitle}/{expenseAmount}/{expenseCategory}",
            arguments = listOf(
                navArgument("expenseTitle") {
                    type = NavType.StringType
                },
                navArgument("expenseAmount") {
                    type = NavType.FloatType
                },
                navArgument("expenseCategory") {
                    type = NavType.StringType
                }


            )
        ) {
            editExpenseScreen(
                navController = navController,
                expenseTitle = it.arguments?.getString("expenseTitle")!!,
                expenseAmount = it.arguments?.getFloat("expenseAmount")!!,
                expenseCategory = it.arguments?.getString("expenseCategory")!!,
                expenseViewModel = expenseViewModel,
                sharedViewModel = sharedViewModel
            )
        }
    }
}

