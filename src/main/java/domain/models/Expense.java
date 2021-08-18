package domain.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private BigDecimal id;
    private BigDecimal price;
    private String description;
    private LocalDate expenseDate;

    public Expense(BigDecimal id, BigDecimal price, String description, LocalDate expenseDate) {
        this.id = id;
        this.price = price;
        this.description = description;
        this.expenseDate = expenseDate;
    }

    public BigDecimal getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }
}