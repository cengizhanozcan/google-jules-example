package com.example.stockapp.controller;

import com.example.stockapp.model.Stock;
import com.example.stockapp.repository.StockRepository;
import com.example.stockapp.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final StockRepository stockRepository;

    @PostMapping("/fetch/{symbol}")
    public ResponseEntity<Stock> fetchStock(@PathVariable String symbol) {
        Stock stock = stockService.fetchAndSaveStockData(symbol);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}
