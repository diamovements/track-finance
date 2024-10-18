package org.example;

import org.example.entity.Category;
import org.example.entity.Transaction;
import org.example.entity.TransactionType;
import org.example.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void getAllTransactionsTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(10000.0));
        transaction.setTransactionType(TransactionType.INCOME);
        transaction.setCategory(new Category(UUID.fromString("00000000-0000-0000-0000-000000000001"), "income", true, userId));
        transaction.setDescription("salary");
        transactions.add(transaction);

        when(transactionService.getAllTransactions(userId)).thenReturn(transactions);

        mockMvc.perform(get("/api/v1/transactions/all")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value("10000.0"))
                .andExpect(jsonPath("$[0].transactionType").value("INCOME"))
                .andExpect(jsonPath("$[0].category.name").value("income"));
    }

    @Test
    public void addTransactionTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        mockMvc.perform(post("/api/v1/transactions/add")
                    .param("userId", userId.toString())
                    .param("amount", "10000.0")
                    .param("type", "INCOME")
                    .param("categoryName", "income")
                    .param("description", "salary"))
                .andExpect(status().isOk());
    }

    @Test
    public void calculateTotalExpenseTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        when(transactionService.calculateTotalExpense(userId)).thenReturn(BigDecimal.valueOf(10000.0));

        mockMvc.perform(get("/api/v1/transactions/total-expense")
                .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void calculateTotalIncomeTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        when(transactionService.calculateTotalIncome(userId)).thenReturn(BigDecimal.valueOf(10000.0));

        mockMvc.perform(get("/api/v1/transactions/total-income")
                .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }
}
