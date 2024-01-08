package com.example.expensetracker

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.expensetracker.RoomDatabase.ExpenseEntity
import com.example.expensetracker.RoomDatabase.ExpenseViewModel
import com.example.expensetracker.ui.theme.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel,
    sharedViewModel: SharedViewModel,
    state: ExpensesState,
    onEvent: (ExpenseEvent) -> Unit
) {




    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddExpense.route)
            },
                containerColor = Color.Black,
                contentColor = Color.White

            )
            {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add expense",

                )

            }
        }
    ){
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
        ){
            Row {

                Image(
                    painter = painterResource(id = R.drawable.images),
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Text(
                    text = "Hi, User",
                    modifier = Modifier
                        .padding(
                            top = 25.dp
                        ),
                    fontSize = 20.sp,
                    color = Color.White
                )

            }

            

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .align(Alignment.BottomStart)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            ){
                LazyColumn {
                    item {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            SortType.values().forEach { sortType ->  
                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(ExpenseEvent.SortContacts(sortType))
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                        RadioButton(
                                            selected = state.sortType == sortType,
                                            onClick = {
                                                onEvent(ExpenseEvent.SortContacts(sortType))
                                            }
                                        )
                                    Text(text = sortType.name)
                                    
                                }
                            }

                        }
                    }

                    items(state.expenses) { expenses ->


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {


                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Expense Icon",
                                    modifier = Modifier
                                        .clickable {
                                            sharedViewModel.selectedExpense.value = expenses
                                            navController.navigate(
                                                Screen.UpdateExpense.withArgs(
                                                    expenses.title,
                                                    expenses.amount.toString(),
                                                    expenses.category
                                                )
                                            )
                                        }
                                        .size(30.dp)
                                        .padding(end = 8.dp)
                                )


                                Text(
                                    text = expenses.title,
                                    modifier = Modifier.padding(end = 8.dp),
                                    fontSize = 20.sp
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = "$" + expenses.amount.toString(),
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                    ,
                                    fontSize = 20.sp
                                    )


                            }

                            Row {
                                Text(
                                    text = expenses.category,
                                    modifier = Modifier
                                        .padding(
                                            bottom = 5.dp,
                                            start = 40.dp
                                        )
                                )
                            }
                        }

                    }
                }
            }
        }
    }




}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {
    var selectedOption by remember {
        mutableStateOf("")
    }

    var expenseTitle by remember {
        mutableStateOf("")
    }

    var expenseAmount by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .align(Alignment.Center)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp,
                        bottomEnd = 30.dp,
                        bottomStart = 30.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {

                TextField(
                    value = expenseTitle,
                    onValueChange = { expenseTitle = it },
                    label = { Text(text = "Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                TextField(
                    value = expenseAmount,
                    onValueChange = { expenseAmount = it },
                    label = { Text(text = "Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                ExpenseCategory_ExposedDropdownMenuBox(
                    selectedOption = selectedOption
                ) { newOption ->
                    selectedOption = newOption
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }



                Button(
                    onClick = {
                        if (expenseAmount.isBlank() || expenseAmount.toFloatOrNull() == null) {
                            // Handle invalid input, show an error message, etc.
                            errorMessage = "Please enter a valid decimal number input"

                        } else {
                            val newExpense = ExpenseEntity(
                                title = expenseTitle,
                                amount = expenseAmount.toFloat(),
                                category = selectedOption
                            )

                            expenseViewModel.insert(newExpense)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.End)

                    ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black

                    )
                ) {
                    Text(
                        text = "Save",
                        color = Color.White
                    )
                }


            }
        }
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editExpenseScreen(
    navController: NavController,
    expenseTitle: String,
    expenseAmount: Float,
    expenseCategory: String,
    expenseViewModel: ExpenseViewModel,
    sharedViewModel: SharedViewModel
) {
    var expenseTitleCurrent by remember {
        mutableStateOf(expenseTitle)
    }

    var expenseAmountCurrent by remember {
        mutableStateOf(expenseAmount.toString())
    }

    var expenseCategoryCurrent by remember {
        mutableStateOf(expenseCategory)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    )
    {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .align(Alignment.Center)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp,
                        bottomEnd = 30.dp,
                        bottomStart = 30.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center
            ){
                TextField(
                    value = expenseTitleCurrent,
                    onValueChange = { expenseTitleCurrent = it },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()

                )

                TextField(
                    value = expenseAmountCurrent,
                    onValueChange = { expenseAmountCurrent = it },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )

                ExpenseCategory_ExposedDropdownMenuBox(
                    selectedOption = expenseCategoryCurrent
                ) { newOption ->
                    expenseCategoryCurrent = newOption
                }


                val selectedExpense = sharedViewModel.selectedExpense.value

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        onClick = {
                            if (selectedExpense != null) {
                                expenseViewModel.delete(selectedExpense)
                            } else {
                                println("No expense selected")
                            }
                            navController.navigate(Screen.ExpenseHome.route)
                        },
                        modifier = Modifier.padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )

                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Expense"
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))


                    Button(
                        onClick = {
                            if (selectedExpense != null) {
                                expenseViewModel.update(
                                    selectedExpense.id,
                                    expenseTitleCurrent,
                                    expenseAmountCurrent.toFloat(),
                                    expenseCategoryCurrent
                                )
                            }
                            navController.navigate(Screen.ExpenseHome.route)
                        },
                        modifier = Modifier.padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )


                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Update Expense"
                        )
                    }


                }


            }


        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCategory_ExposedDropdownMenuBox(
    selectedOption: String,
    changeOption: (String) -> Unit
) {
    val context = LocalContext.current
    val expenseCategories = arrayOf("Food", "Entertainment", "Transportation", "Housing", "Groceries", "Clothing", "Insurance", "Utilities")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                expenseCategories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            changeOption(item)
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}


