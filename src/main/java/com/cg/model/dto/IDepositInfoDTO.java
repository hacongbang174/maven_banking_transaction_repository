package com.cg.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface IDepositInfoDTO {
    Long getId();
    String getDepositerName();
    BigDecimal getTransactionAmount();
    Date getCreatedAt();
}
