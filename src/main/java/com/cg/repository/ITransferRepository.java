package com.cg.repository;

import com.cg.model.Transfer;
import com.cg.model.dto.TransferInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ITransferRepository extends JpaRepository<Transfer, Long> {
    @Query(value = "SELECT NEW com.cg.model.dto.TransferInfoDTO (" +
            "t.transferId, " +
            "t.senderId, " +
            "t.senderName, " +
            "t.recipientId, " +
            "t.recipientName, " +
            "t.transferAmount, " +
            "t.transactionAmount, " +
            "t.fees, " +
            "t.feesAmount) " +
            "FROM vw_transfer_info AS t", nativeQuery = true)
    List<TransferInfoDTO> getTransferInfo();
    @Query(value = "CALL transfer(:senderID, :recipientID, :amount)",nativeQuery = true)
    boolean transfer(@Param("senderID") Long senderID, @Param("recipientID") Long recipientID, @Param("amount") BigDecimal amount);
}
