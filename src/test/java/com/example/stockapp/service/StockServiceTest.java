package com.example.stockapp.service;

import com.example.stockapp.model.Stock;
import com.example.stockapp.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    void fetchAndSaveStockData_success() throws IOException {
        // Given
        String symbol = "AAPL";
        yahoofinance.Stock mockYahooStock = mock(yahoofinance.Stock.class);
        StockQuote mockStockQuote = mock(StockQuote.class);

        when(mockYahooStock.getSymbol()).thenReturn(symbol);
        when(mockYahooStock.getQuote()).thenReturn(mockStockQuote);
        when(mockStockQuote.getPrice()).thenReturn(new BigDecimal("150.00"));

        try (MockedStatic<YahooFinance> mockedStatic = mockStatic(YahooFinance.class)) {
            mockedStatic.when(() -> YahooFinance.get(symbol)).thenReturn(mockYahooStock);

            // When
            stockService.fetchAndSaveStockData(symbol);

            // Then
            verify(stockRepository, times(1)).save(any(Stock.class));
        }
    }

    @Test
    void fetchAndSaveStockData_failure() throws IOException {
        // Given
        String symbol = "FAIL";
        try (MockedStatic<YahooFinance> mockedStatic = mockStatic(YahooFinance.class)) {
            mockedStatic.when(() -> YahooFinance.get(symbol)).thenThrow(new IOException("Test exception"));

            // When
            stockService.fetchAndSaveStockData(symbol);

            // Then
            verify(stockRepository, never()).save(any(Stock.class));
        }
    }
}
