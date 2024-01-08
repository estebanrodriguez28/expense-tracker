package com.example.expensetracker.RoomDatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.ExpenseEvent
import com.example.expensetracker.ExpensesState
import com.example.expensetracker.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.TITLE)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _expenses = _sortType.flatMapLatest { sortType ->
        when(sortType) {
            SortType.TITLE -> expenseDao.sortExpenseByTitle()
            SortType.AMOUNT -> expenseDao.sortExpenseByAmount()
            SortType.CATEGORY -> expenseDao.sortExpenseByCategory()
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ExpensesState())
    val state = combine(_state, _sortType, _expenses) { state, sortType, expenses ->
        state.copy(
            expenses = expenses,
            sortType = sortType
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpensesState())





    fun insert(expense: ExpenseEntity) = viewModelScope.launch {
        expenseDao.insertExpense(expense)
    }

    fun delete(expense: ExpenseEntity) = viewModelScope.launch {
        expenseDao.deleteExpense(expense)
    }

    fun update(expenseId: Long, newTitle: String, newAmount: Float, newCategory: String) = viewModelScope.launch {
        val oldExpense = expenseDao.getExpenseID(expenseId)
        oldExpense?.let {
            it.title = newTitle
            it.amount = newAmount
            it.category = newCategory
            expenseDao.updateExpense(it)
        }

    }



    fun onEvent(event : ExpenseEvent) {
        when(event) {
            is ExpenseEvent.SortContacts -> {
                _sortType.value = event.sortType
            }
        }
    }


}