package com.cg.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;
    private Long createdBy;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
    @UpdateTimestamp
    private Date updatedAt;
    private Long updatedBy;
    @Column(nullable = false)
    private BigDecimal fees;
    @Column(nullable = false)
    private BigDecimal feesAmount;

    @Column(nullable = false, precision = 12)
    @NotNull(message = "Total amount must NOT be empty.")
    @Digits(integer = 12, fraction = 0,
            message = "Maximum digit of Total amount is 12.")
    private BigDecimal transactionAmount;

    @Column(nullable = false, precision = 12)
    @NotNull(message = "Transaction amount must NOT be empty.")
    @Digits(integer = 12, fraction = 0,
            message = "Maximum digit of transaction amount is 12.")
    @Min(value = 100,message = "Transaction amount must NOT be LESS than 100.")
    private BigDecimal transferAmount;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private Customer recipient;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Customer sender;

    public Transfer() {
        this.sender = new Customer((long) 0);
        this.recipient = new Customer((long) 0);
    }

    public Transfer(Long id, Date createdAt, Long createdBy, boolean deleted, Date updatedAt, Long updatedBy, BigDecimal fees, BigDecimal feesAmount, BigDecimal transactionAmount, BigDecimal transferAmount, Customer recipient, Customer sender) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.deleted = deleted;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.fees = fees;
        this.feesAmount = feesAmount;
        this.transactionAmount = transactionAmount;
        this.transferAmount = transferAmount;
        this.recipient = recipient;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public BigDecimal getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(BigDecimal feesAmount) {
        this.feesAmount = feesAmount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Customer getRecipient() {
        return recipient;
    }

    public void setRecipient(Customer recipient) {
        this.recipient = recipient;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }
}

