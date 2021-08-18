package domain.usecases;

import domain.models.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository {
    void AddExpense(Expense newExpense);
    void AddExpenseList(List<Expense> expenseList);
    Expense FindExpenseById(int expenseId);
    List<Expense> FindByDate(LocalDate expenseDate);
    void RemoveExpense(int expenseId);
}
