package com.cg.service.transfer;

import com.cg.model.Transfer;
import com.cg.model.dto.TransferInfoDTO;
import com.cg.service.IGenaralService;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface ITransferService extends IGenaralService<Transfer> {
    List<TransferInfoDTO> getTransferInfo ();
    boolean transfer(long senderID, long recipientID, BigDecimal amount);
}
