package com.cg.service.withdraw;

import com.cg.model.Withdraw;
import com.cg.model.dto.IDepositInfoDTO;
import com.cg.model.dto.IWithDrawInfoDTO;
import com.cg.service.IGenaralService;

import java.math.BigDecimal;
import java.util.List;

public interface IWithDrawService extends IGenaralService<Withdraw> {
    List<IWithDrawInfoDTO> getWithDrawInfo ();
    boolean withdraw (Long id, BigDecimal amount);
}
