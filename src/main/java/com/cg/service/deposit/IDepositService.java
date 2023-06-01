package com.cg.service.deposit;

import com.cg.model.Deposit;
import com.cg.model.dto.IDepositInfoDTO;
import com.cg.model.dto.ITransferInfoDTO;
import com.cg.service.IGenaralService;

import java.math.BigDecimal;
import java.util.List;

public interface IDepositService extends IGenaralService<Deposit> {
    List<IDepositInfoDTO> getDepositInfo ();
    boolean deposit(Long id, BigDecimal amount);
}
