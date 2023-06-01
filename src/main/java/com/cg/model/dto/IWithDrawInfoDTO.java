package com.cg.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface IWithDrawInfoDTO {
    Long getId();
    String getWithDrawerName();
    BigDecimal getTransactionAmount();
    Date getCreatedAt();
}
