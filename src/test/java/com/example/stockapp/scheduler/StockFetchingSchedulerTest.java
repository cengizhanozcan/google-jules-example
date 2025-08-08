package com.example.stockapp.scheduler;

import com.example.stockapp.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StockFetchingSchedulerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockFetchingScheduler stockFetchingScheduler;

    @Test
    void fetchStockData() {
        // Given
        List<String> symbols = Arrays.asList("AAPL", "GOOGL");
        ReflectionTestUtils.setField(stockFetchingScheduler, "stockSymbols", symbols);

        // When
        stockFetchingScheduler.fetchStockData();

        // Then
        verify(stockService, times(1)).fetchAndSaveStockData("AAPL");
        verify(stockService, times(1)).fetchAndSaveStockData("GOOGL");
    }
}
