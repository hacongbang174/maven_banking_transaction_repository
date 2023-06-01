package com.cg.controller;

import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.IDepositInfoDTO;
import com.cg.model.dto.ITransferInfoDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
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
public class DepositController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @GetMapping("/deposit/{id}")
    public ModelAndView showDeposit(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/customers/deposit");
        if (ParsingValidationUtils.isLongParsable(id)) {
            long validId = Long.parseLong(id);
            if (customerService.existsByIdAndDeletedFalse(validId)) {
                CustomerDTO currentCustomer = customerService.findCustomerDTOById(validId);
                modelAndView.addObject("customer", currentCustomer);
                return modelAndView;
            }
            modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        }
        modelAndView.addObject("customer", new CustomerDTO());
        modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        return modelAndView;
    }

    @PostMapping("/deposit/{id}")
    public ModelAndView depositAmount(@PathVariable Long id, @RequestParam("deposit") String deposit) {
        List<String> errors = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/deposit");
        CustomerDTO currentCustomer = customerService.findCustomerDTOById(id);
        if (ParsingValidationUtils.isLongParsable(deposit)) {
            Long depositAmount = Long.parseLong(deposit);
            AmountValidationUtils.validateAmount(depositAmount, errors);
            if (errors.isEmpty()) {
                BigDecimal balance = currentCustomer.getBalance().add(BigDecimal.valueOf(depositAmount));
                if (balance.toString().length() > 12) {
                    errors.add("Exceeded deposit limit. Total deposit is less than 12 digits.");
                } else {
                    currentCustomer.setBalance(balance);
                    depositService.deposit(id, BigDecimal.valueOf(depositAmount));
                    modelAndView.addObject("success", "Successful operation!");
                }
            }
        } else {
            errors.add("Invalid deposit amount, amount must be numeric and less than 12 digits.");
        }
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("customer", currentCustomer);
        return modelAndView;
    }
    @GetMapping("/depositInfo")
    public ModelAndView showDepositInfo() {
        ModelAndView modelAndView = new ModelAndView("/customers/listDepositInfo");
        List<IDepositInfoDTO> deposits = depositService.getDepositInfo();
        modelAndView.addObject("deposits", deposits);
        return modelAndView;
    }
}
