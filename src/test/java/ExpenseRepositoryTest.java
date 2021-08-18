import domain.models.Expense;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ExpenseRepositoryStub implements domain.usecases.ExpenseRepository {
    private List<Expense> DbContext;

    public ExpenseRepositoryStub(List<Expense> expenseDbContext ) {
        this.DbContext = expenseDbContext;
    }

    @Override
    public void AddExpense(Expense newExpense) {
        this.DbContext.add(newExpense);
    }

    @Override
    public void AddExpenseList(List<Expense> expenseList) {
        expenseList.forEach(expense -> {
            this.DbContext.add(expense);
        });
    }

    @Override
    public Expense FindExpenseById(int expenseId) {
        List<Expense> expenseFromDatabase = this.DbContext.stream().filter(expense -> expense.getId().intValue() == expenseId).collect(Collectors.toList());
        return expenseFromDatabase.get(0);
    }

    @Override
    public List<Expense> FindByDate(LocalDate expenseDate) {
        return this.DbContext.stream().filter(expense -> expense.getExpenseDate().equals(expenseDate)).collect(Collectors.toList());
    }

    @Override
    public void RemoveExpense(int expenseId) {
        this.DbContext.removeIf(expense -> expense.getId().intValue() == expenseId);
    }
}

class SystemUnderTest {
    private List persistenceExpenseArray = new ArrayList();

    private ExpenseRepositoryStub expenseRepository = new ExpenseRepositoryStub(persistenceExpenseArray);

    public ExpenseRepositoryStub makeExpenseRepository() {
        return expenseRepository;
    }

    public Expense mockNewExpense(BigDecimal mockId, BigDecimal mockPrice, LocalDate expenseDate) {
        return new Expense(
                mockId,
                mockPrice,
                "any_description",
                expenseDate
        );
    }

    public List<Expense> mockExpenseList() {
        List<Expense> mockedExpenseList = new ArrayList<>();
        Expense expense_A = this.mockNewExpense(new BigDecimal(123), new BigDecimal(120.00), LocalDate.of(2021, 8, 17));
        Expense expense_B = this.mockNewExpense(new BigDecimal(456), new BigDecimal(134.12), LocalDate.of(2021, 8, 17));
        Expense expense_C = this.mockNewExpense(new BigDecimal(789), new BigDecimal(166.66), LocalDate.of(2021, 8, 17));
        mockedExpenseList.add(expense_A);
        mockedExpenseList.add(expense_B);
        mockedExpenseList.add(expense_C);
        return mockedExpenseList;
    }

    public int persistenceLength() {
        return persistenceExpenseArray.size();
    }
}

public class ExpenseRepositoryTest {

    @Test
    public void should_add_a_new_expense() {
        SystemUnderTest sut = new SystemUnderTest();
        sut.makeExpenseRepository().AddExpense(sut.mockNewExpense(new BigDecimal(123), new BigDecimal(56.40), LocalDate.of(2021, 8, 17)));
        assertEquals(1, sut.persistenceLength());
    }

    @Test
    public void should_return_expense_by_id() {
        SystemUnderTest sut = new SystemUnderTest();
        Expense mockNewExpense = sut.mockNewExpense(new BigDecimal(456), new BigDecimal(100.80), LocalDate.of(2021, 8, 17));
        ExpenseRepositoryStub repository = sut.makeExpenseRepository();
        repository.AddExpense(mockNewExpense);
        Expense expenseFromDatabase = repository.FindExpenseById(456);
        assertEquals(expenseFromDatabase, mockNewExpense);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void should_throw_if_no_expense_is_found() {
        SystemUnderTest sut = new SystemUnderTest();
        ExpenseRepositoryStub repository = sut.makeExpenseRepository();
        repository.FindExpenseById(789);
    }

    @Test
    public void should_add_a_list_of_new_expenses() {
        SystemUnderTest sut = new SystemUnderTest();
        List<Expense> expenseList = sut.mockExpenseList();
        ExpenseRepositoryStub repository = sut.makeExpenseRepository();
        repository.AddExpenseList(expenseList);
        assertEquals(3, sut.persistenceLength());
    }

    @Test
    public void should_find_expense_by_date() {
        SystemUnderTest sut = new SystemUnderTest();
        List<Expense> expenseList = sut.mockExpenseList();
        ExpenseRepositoryStub repository = sut.makeExpenseRepository();
        repository.AddExpenseList(expenseList);
        List<Expense> expensesByDate = repository.FindByDate(LocalDate.of(2021, 8, 17));
        assertEquals(3, expensesByDate.size());
    }

    @Test
    public void should_remove_an_specific_expense() {
        SystemUnderTest sut = new SystemUnderTest();
        List<Expense> expenseList = sut.mockExpenseList();
        ExpenseRepositoryStub repository = sut.makeExpenseRepository();
        repository.AddExpenseList(expenseList);
        repository.RemoveExpense(789);
        repository.RemoveExpense(456);
        assertEquals(1, sut.persistenceLength());
    }
}
