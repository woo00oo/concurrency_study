package com.example.stock.facade;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class NamedLockStockFacadeTest {

    @Autowired
    NamedLockStockFacade stockFacade;

    @Autowired
    StockRepository stockRepository;

    @BeforeEach
    void bofore() {
        Stock stock = new Stock(1L, 100L);

        stockRepository.saveAndFlush(stock);

    }

    @AfterEach
    void after() {
        stockRepository.deleteAll();
    }


    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i=0; i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockFacade.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

}