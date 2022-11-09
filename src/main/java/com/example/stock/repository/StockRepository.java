package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     * SELECT FOR ~ UPDATE = 동시성 제어를 위해 특정 row에 배타적 LOCK을 거는 행위
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithPessimisticLock(Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithOptimisticLock(Long id);
}

/**
 *
 * MySQL
 *
 * 1. Pessimistic Lock
 * - 실제로 데이터에 Lock 을 걸어서 정합성을 맞추는 방법.
 *   exclusive lock 을 걸게되면 다른 트랜잭션에서는 lock 이 해제되기전에 데이터를 가져갈 수 없게 된다.
 *   데드락이 걸릴 수 있기 때문에 주의하여 사용하여야 합니다.
 *   로우나 테이블 단위로 Lock 을 건다.
 *   충돌이 빈번할 경우 유용
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
 *   분산 Lock
 *
 * Redis
 *
 * 1. Lettuce
 *  - 구현이 간단한다.
 *  - Spring data redis 를 이용하면 lettuce 가 기본이기 때문에 별도의 라이브러리를 사용하지 않아도 된다.
 *  - spin lock 방식이기 때문에 동시에 많은 쓰레드가 lock 획득 대기 상태라면 redis 에 부하가 갈 수 있다.
 *
 *
 * 2. Redisson
 * - 락 획득 재시도를 기본으로 제공한다.
 * - pub-sub 방식으로 구현이 되어있기 때문에 lettuce 와 비교했을 때 redis 에 부하가 덜 간다.
 * - lock 을 라이브러리 차원에서 제공해주기 때문에 사용법을 공부해야 한다.
 *
 * 실무에서는 ?
 * - 재시도가 필요하지 않은 Lock 은 lettuce 활용
 * - 재시도가 필요한 경우에는 redisson 을 활용
 *
 *
 * MySQL
 *  - 이미 MySQL을 사용하고 있다면 별도의 비용없이 사용 가능.
 *  - 어느정도의 트래픽까지는 문제없이 활용 가능.
 *  - Redis 보다는 성능이 좋지 않다.
 *
 *  Redis
 *  - 활용중인 Redis 가 없다면 별도의 구축비용과 인프라 관리비용이 발생.
 *  - MySQL 보다 성능이 좋다.
 **/