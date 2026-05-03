package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.CustomerOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JpaCustomerOrderRepository extends JpaRepository<CustomerOrderEntity, Long> {
    Optional<CustomerOrderEntity> findFirstByTableIdAndStatusOrderByCreatedAtDesc(Long tableId, String status);
    List<CustomerOrderEntity> findByTableIdInAndStatusOrderByUpdatedAtDesc(Collection<Long> tableIds, String status);
    List<CustomerOrderEntity> findByTableIdOrderByCreatedAtDesc(Long tableId);
    List<CustomerOrderEntity> findByStatusInOrderByUpdatedAtDesc(Collection<String> statuses);
    boolean existsByCreatedByUserId(Long createdByUserId);

    @Modifying
    @Query("update CustomerOrderEntity o set o.createdByUserId = :replacementUserId where o.createdByUserId = :deletedUserId")
    int reassignCreatedOrders(
            @Param("deletedUserId") Long deletedUserId,
            @Param("replacementUserId") Long replacementUserId
    );
}
