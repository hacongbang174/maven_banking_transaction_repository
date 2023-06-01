package com.cg.repository;

import com.cg.model.Withdraw;
import com.cg.model.dto.IDepositInfoDTO;
import com.cg.model.dto.IWithDrawInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IWithDrawRepository extends JpaRepository<Withdraw, Long> {
    @Query(value = "SELECT * FROM vw_withdraw_info", nativeQuery = true)
    List<IWithDrawInfoDTO> getWithDrawInfo();
    @Query(value = "CALL withdraw(:id, :amount)",nativeQuery = true)
    boolean withdraw(@Param("id") long id, @Param("amount") BigDecimal amount);
}
