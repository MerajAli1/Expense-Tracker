package com.businesstracker.backend;

import com.businesstracker.backend.dto.BalanceUpdateRequest;
import com.businesstracker.backend.dto.BorrowRequest;
import com.businesstracker.backend.dto.ReceiptProcessRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllExpenses() throws Exception {
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", isA(java.util.List.class)))
                .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(1)));
    }

    @Test
    void testCreateExpense() throws Exception {
        BaseExpense newExpense = new BaseExpense(
                null,
                "Cloud Server Hosting",
                8500.0,
                "Utility",
                "AWS EC2 production instance",
                LocalDate.now(),
                "Credit Card",
                true,
                0.18
        );

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.title", is("Cloud Server Hosting")))
                .andExpect(jsonPath("$.data.baseAmount", is(8500.0)))
                .andExpect(jsonPath("$.data.taxAmount", is(1530.0)))
                .andExpect(jsonPath("$.data.totalAmount", is(10030.0)));
    }

    @Test
    void testGetExpenseSummary() throws Exception {
        mockMvc.perform(get("/api/expenses/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalExpenses", greaterThan(0.0)))
                .andExpect(jsonPath("$.data.transactionCount", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.data.categoryBreakdown", notNullValue()));
    }

    @Test
    void testGetVaultState() throws Exception {
        mockMvc.perform(get("/api/vault"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", notNullValue()));
    }

    @Test
    void testProcessReceiptTaxIsolation() throws Exception {
        ReceiptProcessRequest request = new ReceiptProcessRequest(
                "Vendor Software License",
                11800.0,
                0.18,
                "Software",
                "JetBrains Team Pack Subscription"
        );

        mockMvc.perform(post("/api/vault/process-receipt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.receiptAmount", is(11800.0)))
                .andExpect(jsonPath("$.data.baseExpense", is(10000.0)))
                .andExpect(jsonPath("$.data.isolatedTax", is(1800.0)));
    }

    @Test
    void testSavingsBorrowAndReturn() throws Exception {
        BorrowRequest borrow = new BorrowRequest(5000.0, "Office Coffee Machine Repair");

        String response = mockMvc.perform(post("/api/savings/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrow)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.borrowedAmount", is(5000.0)))
                .andExpect(jsonPath("$.data.returned", is(false)))
                .andReturn().getResponse().getContentAsString();

        String transactionId = objectMapper.readTree(response).path("data").path("transactionId").asText();

        mockMvc.perform(put("/api/savings/" + transactionId + "/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.returned", is(true)));
    }

    @Test
    void testGetUserDetailsAndBalanceUpdate() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.currency", is("PKR")));

        BalanceUpdateRequest update = new BalanceUpdateRequest(220000.0, 110000.0);
        mockMvc.perform(put("/api/user/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.businessBalance", is(220000.0)))
                .andExpect(jsonPath("$.data.savingsBalance", is(110000.0)));
    }
}
