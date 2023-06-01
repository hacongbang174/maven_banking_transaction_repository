package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.ITransferInfoDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
import com.cg.utils.AmountValidationUtils;
import com.cg.utils.ParsingValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class TransferController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ITransferService transferService;

    @GetMapping("/transfer/{id}")
    public ModelAndView showTransfer(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/customers/transfer");
        if (ParsingValidationUtils.isLongParsable(id)) {
            long validId = Long.parseLong(id);
            if (customerService.existsByIdAndDeletedFalse(validId)) {
                CustomerDTO sender = customerService.findCustomerDTOById(validId);
                List<Customer> recipients = customerService.findAllByIdIsNotAndDeletedFalse(validId);
                modelAndView.addObject("sender", sender);
                modelAndView.addObject("recipients", recipients);
                return modelAndView;
            }
            modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        }
        modelAndView.addObject("sender", new CustomerDTO());
        modelAndView.addObject("recipients", new ArrayList<>());
        modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        return modelAndView;
    }

    @PostMapping("/transfer/{id}")
    public ModelAndView transfer(@PathVariable long id, @RequestParam("recipientId") long recipientId, @RequestParam("fees") long fees, @RequestParam("transfer") String transfer) {
        List<String> errors = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/transfer");
        CustomerDTO customerSender = customerService.findCustomerDTOById(id);
        CustomerDTO customerRecipient = customerService.findCustomerDTOById(recipientId);
        List<Customer> recipients = customerService.findAllByIdIsNotAndDeletedFalse(id);
        if (ParsingValidationUtils.isLongParsable(transfer)) {
            Long transferAmount = Long.parseLong(transfer);
            AmountValidationUtils.validateAmount(transferAmount, errors);
            if (errors.isEmpty()) {
                long fees_amount = (fees * transferAmount) / 100;
                long transaction_amount = transferAmount + fees_amount;

                if (customerSender.getBalance().compareTo(BigDecimal.valueOf(transaction_amount)) < 0) {
                    errors.add("Số dư hiện tại không đủ. Vui lòng chuyển số tiền nhỏ hơn " + customerSender.getBalance().subtract(BigDecimal.valueOf(fees_amount)));
                } else {
                    BigDecimal balanceSender = customerSender.getBalance().subtract(BigDecimal.valueOf(transaction_amount));
                    BigDecimal balanceRecipent = customerRecipient.getBalance().add(BigDecimal.valueOf(transferAmount));
                    if (balanceRecipent.toString().length() > 12) {
                        errors.add("Vượt quá định mức tổng tiền người nhận. Tổng tiền gửi nhỏ hơn 12 chữ số.");
                    } else {
                        customerSender.setBalance(balanceSender);
                        customerRecipient.setBalance(balanceRecipent);
                        if (transferService.transfer(id, recipientId, BigDecimal.valueOf(transferAmount))) {
                            modelAndView.addObject("success", "Successful operation!");
                        }
                    }
                }
            }
            modelAndView.addObject("sender", customerSender);
            modelAndView.addObject("recipients", recipients);
            modelAndView.addObject("errors", errors);
            return modelAndView;
        } else {
            errors.add("Invalid withdrawal amount, amount must be numeric and less than 12 digits");
        }
        modelAndView.addObject("sender", customerSender);
        modelAndView.addObject("recipients", recipients);
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }

    @GetMapping("/transferInfo")
    public ModelAndView showTransfer() {
        ModelAndView modelAndView = new ModelAndView("/customers/listTransfer");
        List<ITransferInfoDTO> transfers = transferService.getTransferInfo();
        modelAndView.addObject("transfers", transfers);
        return modelAndView;
    }
}
