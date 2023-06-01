package com.cg.service.deposit;

import com.cg.model.Deposit;
import com.cg.model.dto.IDepositInfoDTO;
import com.cg.repository.IDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DepositService implements IDepositService{

    @Autowired
    private IDepositRepository depositRepository;

    @Override
    public List<Deposit> findAll() {
        return null;
    }

    @Override
    public Optional<Deposit> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Deposit getById(Long id) {
        return null;
    }

    @Override
    public void save(Deposit deposit) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public List<IDepositInfoDTO> getDepositInfo() {
        return depositRepository.getDepositInfo();
    }

    @Override
    public boolean deposit(Long id, BigDecimal amount) {
        return depositRepository.deposit(id,amount);
    }
}
