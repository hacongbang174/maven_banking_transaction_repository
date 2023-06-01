package com.cg.service.withdraw;

import com.cg.model.Withdraw;
import com.cg.model.dto.IWithDrawInfoDTO;
import com.cg.repository.IWithDrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WithDrawService implements IWithDrawService {

    @Autowired
    private IWithDrawRepository withDrawRepository;
    @Override
    public List<Withdraw> findAll() {
        return null;
    }

    @Override
    public Optional<Withdraw> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Withdraw getById(Long id) {
        return null;
    }

    @Override
    public void save(Withdraw withdraw) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public List<IWithDrawInfoDTO> getWithDrawInfo() {
        return withDrawRepository.getWithDrawInfo();
    }

    @Override
    public boolean withdraw(Long id, BigDecimal amount) {
        return withDrawRepository.withdraw(id, amount);
    }
}
