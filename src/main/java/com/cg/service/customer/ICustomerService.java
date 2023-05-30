package com.cg.service.customer;


import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import com.cg.service.IGenaralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGenaralService<Customer> {
    List<CustomerDTO> findAllDTO();
    CustomerDTO findCustomerDTOById(long id);

    boolean addNewCustomer(String name, String email, String phone, String address);

    boolean updateCustomer(long id, String name, String email, String phone, String address);

    boolean existsByIdAndDeletedFalse(long id);

    void suspendCustomer(long id);

    void deposit (long id, BigDecimal amount);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<Customer> findAllByIdIsNotAndDeletedFalse(long id);

}
