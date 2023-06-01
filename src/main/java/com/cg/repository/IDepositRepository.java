package com.cg.repository;

import com.cg.model.Deposit;
import com.cg.model.dto.IDepositInfoDTO;
import com.cg.model.dto.ITransferInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IDepositRepository extends JpaRepository<Deposit, Long> {
    @Query(value = "SELECT * FROM vw_deposit_info", nativeQuery = true)
    List<IDepositInfoDTO> getDepositInfo();
    @Query(value = "CALL deposit(:id, :amount)",nativeQuery = true)
    boolean deposit(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
