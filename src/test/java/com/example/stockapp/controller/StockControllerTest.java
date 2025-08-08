package com.example.stockapp.controller;

import com.example.stockapp.model.Stock;
import com.example.stockapp.repository.StockRepository;
import com.example.stockapp.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @MockBean
    private StockRepository stockRepository;

    @Test
    void fetchStock_success() throws Exception {
        // Given
        String symbol = "AAPL";
        Stock stock = new Stock(1L, symbol, new BigDecimal("150.00"), null);
        given(stockService.fetchAndSaveStockData(symbol)).willReturn(stock);

        // When & Then
        mockMvc.perform(post("/api/stocks/fetch/{symbol}", symbol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is(symbol)));
    }

    @Test
    void fetchStock_notFound() throws Exception {
        // Given
        String symbol = "FAIL";
        given(stockService.fetchAndSaveStockData(symbol)).willReturn(null);

        // When & Then
        mockMvc.perform(post("/api/stocks/fetch/{symbol}", symbol))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllStocks() throws Exception {
        // Given
        Stock stock = new Stock(1L, "AAPL", new BigDecimal("150.00"), null);
        given(stockRepository.findAll()).willReturn(Collections.singletonList(stock));

        // When & Then
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].symbol", is("AAPL")));
    }
}
