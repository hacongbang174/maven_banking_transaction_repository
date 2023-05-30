package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
import com.cg.utils.AmountValidationUtils;
import com.cg.utils.ParsingValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ITransferService transferService;

    @GetMapping("")
    public ModelAndView showCustomerList() {
        ModelAndView modelAndView = new ModelAndView("/customers/list");
        List<CustomerDTO> customerList = customerService.findAllDTO();
        modelAndView.addObject("customerList", customerList);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/customers/create");
        CustomerDTO newCustomerDTO = new CustomerDTO();
        modelAndView.addObject("customerDTO", newCustomerDTO);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createCustomer(@Validated @ModelAttribute("customerDTO") CustomerDTO customerDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("/customers/create");

        String name = customerDTO.getFullName().trim();
        String phone = customerDTO.getPhone().trim();
        String email = customerDTO.getEmail().trim().toLowerCase();
        String address = customerDTO.getAddress().trim();

        if (customerService.existsByPhone(phone)) {
            bindingResult.addError(new ObjectError("phoneExists", "Phone number has existed!"));
        }

        if (customerService.existsByEmail(email)) {
            bindingResult.addError(new ObjectError("emailExists", "Email address has existed!"));
        }

        if (!bindingResult.hasErrors()) {
            if (customerService.addNewCustomer(name, email, phone, address)) {
                modelAndView.addObject("success", "Successful operation!");
            }
            modelAndView.addObject("customerDTO", new CustomerDTO());
        } else {
            modelAndView.addObject("hasError", true);
            modelAndView.addObject("bindingResult", bindingResult);
            modelAndView.addObject("customerDTO", new CustomerDTO());
        }
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/customers/edit");
        if (ParsingValidationUtils.isLongParsable(id)) {
            long validId = Long.parseLong(id);
            if (customerService.existsByIdAndDeletedFalse(validId)) {
                CustomerDTO currentCustomer = customerService.findCustomerDTOById(validId);
                modelAndView.addObject("currentCustomer", currentCustomer);
                return modelAndView;
            }
            modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        }
        modelAndView.addObject("currentCustomer", new CustomerDTO());
        modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editCustomer(@PathVariable Long id, @Validated @ModelAttribute("currentCustomer") CustomerDTO currentCustomer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("/customers/edit");

        String name = currentCustomer.getFullName().trim();
        String phone = currentCustomer.getPhone().trim();
        String email = currentCustomer.getEmail().trim().toLowerCase();
        String address = currentCustomer.getAddress().trim();

        if (customerService.existsByPhone(phone)) {
            bindingResult.addError(new ObjectError("phoneExists", "Phone number has existed!"));
        }

        if (customerService.existsByEmail(email)) {
            bindingResult.addError(new ObjectError("emailExists", "Email address has existed!"));
        }

        if (!bindingResult.hasErrors()) {
            if (customerService.updateCustomer(id, name, email, phone, address)) {
                modelAndView.addObject("success", "Successful operation!");
            }
        } else {
            modelAndView.addObject("hasError", true);
            modelAndView.addObject("bindingResult", bindingResult);
        }
        modelAndView.addObject("currentCustomer", currentCustomer);
        return modelAndView;
    }

    @GetMapping("/suspend/{id}")
    public ModelAndView showSuspendForm(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/customers/suspend");
        if (ParsingValidationUtils.isLongParsable(id)) {
            long validId = Long.parseLong(id);
            if (customerService.existsByIdAndDeletedFalse(validId)) {
                CustomerDTO currentCustomer = customerService.findCustomerDTOById(validId);
                modelAndView.addObject("currentCustomer", currentCustomer);
                return modelAndView;
            }
            modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        }
        modelAndView.addObject("currentCustomer", new CustomerDTO());
        modelAndView.addObject("wrongId", "Customer ID doesn't exist!");
        return modelAndView;
    }

    @PostMapping("/suspend/{id}")
    public ModelAndView suspendCustomer(@PathVariable Long id, @Validated @ModelAttribute("currentCustomer") CustomerDTO currentCustomer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("/customers/suspend");
        customerService.suspendCustomer(id);
        modelAndView.addObject("success", "Successful operation!");
        modelAndView.addObject("currentCustomer", currentCustomer);
        return modelAndView;
    }

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
                    customerService.deposit(id, balance);
                    modelAndView.addObject("success", "Successful operation!");
                }
            }
        } else {
            errors.add("Invalid withdrawal amount, amount must be numeric and less than 12 digits.");
        }
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("customer", currentCustomer);
        return modelAndView;
    }

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
                    customerService.deposit(id, balance);
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
            if(errors.isEmpty()) {
                long fees_amount = (fees * transferAmount) / 100;
                long transaction_amount = transferAmount + fees_amount;

                if(customerSender.getBalance().compareTo(BigDecimal.valueOf(transaction_amount)) < 0) {
                    errors.add("Số dư hiện tại không đủ. Vui lòng chuyển số tiền nhỏ hơn " + customerSender.getBalance().subtract(BigDecimal.valueOf(fees_amount)));
                }else {
                    BigDecimal balanceSender = customerSender.getBalance().subtract(BigDecimal.valueOf(transaction_amount));
                    BigDecimal balanceRecipent = customerRecipient.getBalance().add(BigDecimal.valueOf(transferAmount));
                    if(balanceRecipent.toString().length() > 12) {
                        errors.add("Vượt quá định mức tổng tiền người nhận. Tổng tiền gửi nhỏ hơn 12 chữ số.");
                    }else {
                        customerSender.setBalance(balanceSender);
                        customerRecipient.setBalance(balanceRecipent);
                        transferService.transfer(id, recipientId, BigDecimal.valueOf(transferAmount));
                        modelAndView.addObject("success", "Successful operation!");
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
        List<Transfer> transfers = transferService.findAll();
        modelAndView.addObject("transfers", transfers);
        return modelAndView;
    }
}
