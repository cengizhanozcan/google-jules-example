package com.example.stockapp.service;

import com.example.stockapp.model.Stock;
import com.example.stockapp.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;

    public Stock fetchAndSaveStockData(String symbol) {
        try {
            yahoofinance.Stock stock = YahooFinance.get(symbol);
            if (stock != null && stock.getQuote() != null && stock.getQuote().getPrice() != null) {
                Stock newStock = new Stock();
                newStock.setSymbol(stock.getSymbol());
                newStock.setPrice(stock.getQuote().getPrice());
                return stockRepository.save(newStock);
            } else {
                logger.warn("No stock information found for symbol: {}", symbol);
            }
        } catch (IOException e) {
            logger.error("Error fetching stock data for symbol: {}", symbol, e);
        }
        return null;
    }
}
