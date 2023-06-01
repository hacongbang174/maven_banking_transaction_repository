package com.cg.repository;

import com.cg.model.Transfer;
import com.cg.model.dto.ITransferInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ITransferRepository extends JpaRepository<Transfer, Long> {
    @Query(value = "SELECT * FROM vw_transfer_info", nativeQuery = true)
    List<ITransferInfoDTO> getTransferInfo();
    @Query(value = "CALL transfer(:senderID, :recipientID, :amount)",nativeQuery = true)
    boolean transfer(@Param("senderID") Long senderID, @Param("recipientID") Long recipientID, @Param("amount") BigDecimal amount);
}
