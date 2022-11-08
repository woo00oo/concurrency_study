package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}

/**
 * 1. Pessimistic Lock
 * - 실제로 데이터에 Lock 을 걸어서 정합성을 맞추는 방법.
 *   exclusive lock 을 걸게되면 다른 트랜잭션에서는 lock 이 해제되기전에 데이터를 가져갈 수 없게 된다.
 *   데드락이 걸릴 수 있기 때문에 주의하여 사용하여야 합니다.
 *   로우나 테이블 단위로 Lock 을 건다.
 *
 * 2. Optimistic Lock
 * - 실제로 Lock 을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법.
 *   먼저 데이터를 읽은 후에 update 를 수행할 때 현재 내가 읽은 버전이 맞는지 확인하며 업데이트.
 *   내가 읽은 버전에서 수정사항이 생겼을 경우에는 application 에서 다시 읽은후에 작업을 수행해야 한다.
 *
 * 3. Named Lock
 * - 이름을 가진 metadata locking.
 *   이름을 가진 lock 을 획득한 후 해제할때까지 다른 세션은 이 lock 을 획득할 수 없도록 함.
 *   주의할점은 transaction 이 종료될 때 lock 이 자동으로 해제되지 않는다.
 *   별도의 명령어로 해제를 수행해주거나 선점시간이 끝나야 해제된다.
 **/