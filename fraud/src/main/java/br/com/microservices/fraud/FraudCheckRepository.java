package br.com.microservices.fraud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudCheckRepository extends JpaRepository<FraudCheckHistory, Long> {

//    @Query("""
//            SELECT CASE WHEN count() > 0
//            THEN true ELSE false END
//            FROM FraudCheckHistory f
//            WHERE f.costumerId = :costumerId AND f.isFraud = true""")
//    boolean existsFraudById(@Param("costumerId") Long costumerId);
    boolean existsByCostumerIdAndIsFraudTrue(Long costumerId);
}
