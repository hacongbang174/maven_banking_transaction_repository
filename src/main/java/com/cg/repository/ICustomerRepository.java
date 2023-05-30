package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT NEW com.cg.model.dto.CustomerDTO (" +
            "c.id, " +
            "c.fullName, " +
            "c.email, " +
            "c.phone, " +
            "c.address, " +
            "c.balance, " +
            "c.deleted) " +
            "FROM Customer c " +
            "WHERE c.deleted = false")
    List<CustomerDTO> findAllDTO();

    @Query("SELECT NEW com.cg.model.dto.CustomerDTO (" +
            "c.id, " +
            "c.fullName, " +
            "c.email, " +
            "c.phone, " +
            "c.address, " +
            "c.balance, " +
            "c.deleted) " +
            "FROM Customer c " +
            "WHERE c.id = :id")
    CustomerDTO findCustomerDTOById(@Param("id") long id);

    @Query(value = "CALL insert_customer(:name, :email, :phone, :address)",nativeQuery = true)
    boolean addNewCustomer(@Param("name") String name,@Param("email") String email,@Param("phone") String phone,@Param("address") String address);

    @Query(value = "CALL update_customer(:id, :name, :email, :phone, :address)",nativeQuery = true)
    boolean updateCustomer(@Param("id") long id,@Param("name") String name,@Param("email") String email,@Param("phone") String phone,@Param("address") String address);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c " +
            "SET c.deleted = TRUE " +
            "WHERE c.id = :id")
    void suspendCustomer(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c " +
            "SET c.balance = :amount " +
            "WHERE c.id = :id")
    void deposit(@Param("id") long id, @Param("amount") BigDecimal amount);

    List<Customer> findAllByIdIsNotAndDeletedFalse(long id);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByIdAndDeletedFalse(long id);
}
