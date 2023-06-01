package com.cg.service.transfer;

import com.cg.model.Transfer;
import com.cg.model.dto.ITransferInfoDTO;
import com.cg.service.IGenaralService;

import java.math.BigDecimal;
import java.util.List;


public interface ITransferService extends IGenaralService<Transfer> {
    List<ITransferInfoDTO> getTransferInfo ();
    boolean transfer(long senderID, long recipientID, BigDecimal amount);
}
