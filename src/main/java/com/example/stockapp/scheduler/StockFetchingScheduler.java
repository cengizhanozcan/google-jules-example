package com.example.stockapp.scheduler;

import com.example.stockapp.service.StockService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockFetchingScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StockFetchingScheduler.class);

    private final StockService stockService;

    @Value("${app.stock.symbols}")
    private List<String> stockSymbols;

    @Scheduled(fixedRateString = "${app.schedule.rate:600000}") // Default to 10 minutes
    public void fetchStockData() {
        logger.info("Fetching stock data for symbols: {}", stockSymbols);
        for (String symbol : stockSymbols) {
            try {
                stockService.fetchAndSaveStockData(symbol);
            } catch (Exception e) {
                logger.error("Error fetching data for symbol: {}", symbol, e);
            }
        }
    }
}
