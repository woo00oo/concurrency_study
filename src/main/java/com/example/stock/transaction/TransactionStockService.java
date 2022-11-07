package com.example.stock.transaction;

import com.example.stock.service.StockService;

public class TransactionStockService {

    private StockService stockService;

    public TransactionStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        startTransaction();

        // synchronized 구문 실행
        stockService.decrease(id, quantity);

        // DB에 commit 되기전 다른 쓰레드가 공유 자원에 접근할 수 있음
        endTransaction();

    }

    public void startTransaction() {

    }

    public void endTransaction(){

    }
}
