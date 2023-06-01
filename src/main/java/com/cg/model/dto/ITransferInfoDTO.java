package com.cg.model.dto;

import java.math.BigDecimal;

public interface ITransferInfoDTO {
    Long getId();
    Long getSenderId();
    String getSenderName();
    Long getRecipientId();
    String getRecipientName();
    BigDecimal getTransferAmount();
    BigDecimal getTransactionAmount();
    Integer getFees();
    BigDecimal getFeesAmount();
}
