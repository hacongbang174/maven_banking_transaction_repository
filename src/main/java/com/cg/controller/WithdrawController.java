package com.cg.controller;

import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.IDepositInfoDTO;
import com.cg.model.dto.IWithDrawInfoDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.withdraw.IWithDrawService;
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
public class WithdrawController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IWithDrawService withDrawService;


    @GetMapping("/withdraw/{id}")
    public ModelAndView showWithdraw(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/customers/withdraw");
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

    @PostMapping("/withdraw/{id}")
    public ModelAndView withdrawAmount(@PathVariable Long id, @RequestParam("withdraw") String withdraw) {
        List<String> errors = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/withdraw");
        CustomerDTO customerWithDraw = customerService.findCustomerDTOById(id);
        if (ParsingValidationUtils.isLongParsable(withdraw)) {
            Long withdrawAmount = Long.parseLong(withdraw);
            AmountValidationUtils.validateAmount(withdrawAmount, errors);
            if (errors.isEmpty()) {
                BigDecimal balance = customerWithDraw.getBalance().subtract(BigDecimal.valueOf(withdrawAmount));
                if (customerWithDraw.getBalance().compareTo(BigDecimal.valueOf(withdrawAmount)) < 0) {
                    errors.add("Current balance is not enough. Please withdraw smaller amount " + customerWithDraw.getBalance());
                } else {
                    customerWithDraw.setBalance(balance);
                    withDrawService.withdraw(id, BigDecimal.valueOf(withdrawAmount));
                    modelAndView.addObject("success", "Successful operation!");
                }
            }
        } else {
            errors.add("Invalid withdrawal amount, amount must be numeric and less than 12 digits");
        }
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("customer", customerWithDraw);
        return modelAndView;
    }
    @GetMapping("/withdrawInfo")
    public ModelAndView showDepositInfo() {
        ModelAndView modelAndView = new ModelAndView("/customers/listWithDrawInfo");
        List<IWithDrawInfoDTO> withdraws = withDrawService.getWithDrawInfo();
        modelAndView.addObject("withdraws", withdraws);
        return modelAndView;
    }
}
