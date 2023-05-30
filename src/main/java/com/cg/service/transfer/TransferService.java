package com.cg.service.transfer;

import com.cg.model.Transfer;
import com.cg.model.dto.TransferInfoDTO;
import com.cg.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService implements ITransferService{
    @Autowired
    private ITransferRepository transferRepository;


    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return transferRepository.findById(id);
    }

    @Override
    public Transfer getById(Long id) {
        return transferRepository.getOne(id);
    }

    @Override
    public void save(Transfer transfer) {
        transferRepository.save(transfer);
    }

    @Override
    public void remove(Long id) {
        transferRepository.deleteById(id);
    }

    @Override
    public List<TransferInfoDTO> getTransferInfo() {
        return transferRepository.getTransferInfo();
    }

    @Override
    public boolean transfer(long senderID, long recipientID, BigDecimal amount) {
        return transferRepository.transfer(senderID,recipientID,amount);
    }
}
