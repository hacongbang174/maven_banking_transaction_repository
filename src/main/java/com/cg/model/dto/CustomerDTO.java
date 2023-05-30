package com.cg.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class CustomerDTO {
    private Long id;

    @NotEmpty(message = "Name must NOT be empty!")
    @Length(min = 1, max = 150,
            message = "Max characters of name: 150.")
    @Pattern(regexp = "^[A-Za-z\\s]*",
            message = "Name contains only letter and whitespace.")
    private String fullName;

    @NotEmpty(message = "Email must NOT be empty!")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$",
            message = "Invalid email address. Valid email example: john_11@gmail.com")
    private String email;

    @NotEmpty(message = "Phone number must NOT be empty.")
    @Pattern(regexp = "^0[1-9]\\d{8,9}$",
            message = "Phone Number: First digit must be '0', second digit is form '1' to '9' and length is from 10 to 11 digits.")
    private String phone;
    @NotEmpty(message = "Address must NOT be empty.")
    private String address;

    private BigDecimal balance;
    private boolean deleted;

    public CustomerDTO() {
    }

    public CustomerDTO(long id, String fullName, String email, String phone, String address, BigDecimal balance, boolean deleted) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
        this.deleted = deleted;
    }

    public CustomerDTO(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
