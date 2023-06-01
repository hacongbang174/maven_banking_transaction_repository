package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.ITransferInfoDTO;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

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

        if (customerService.existsByPhoneAndIdIsNot(phone,id)) {
            bindingResult.addError(new ObjectError("phoneExists", "Phone number has existed!"));
        }

        if (customerService.existsByEmailAndIdIsNot(email,id)) {
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




}
