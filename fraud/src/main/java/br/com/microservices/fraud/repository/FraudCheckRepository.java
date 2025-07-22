package br.com.microservices.fraud.repository;

import br.com.microservices.fraud.domain.model.FraudCheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudCheckRepository extends JpaRepository<FraudCheckHistory, Long> {

//    @Query("""
//            SELECT CASE WHEN count() > 0
//            THEN true ELSE false END
//            FROM FraudCheckHistory f
//            WHERE f.customerId = :customerId AND f.isFraud = true""")
//    boolean existsFraudById(@Param("customerId") Long customerId);
    boolean existsBycustomerIdAndIsFraudTrue(Long customerId);
}
