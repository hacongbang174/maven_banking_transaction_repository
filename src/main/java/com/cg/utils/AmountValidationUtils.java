package com.cg.utils;

import java.util.List;
import java.util.regex.Pattern;

public class AmountValidationUtils {
    public static void validateAmount(Long amount, List<String> errors) {
        if (amount.toString().equals("")) {
            errors.add("Amount must NOT be empty.");
        }else{
            if (!Pattern.matches("^(?:1\\d{2,11}|[2-9]\\d{2,11}|1\\d{0,1}|[2-9]\\d{0,2}|[0-9])$", amount.toString())) {
                errors.add("Amount must be greater than 100 and less than or equal to 12 digits.");
            }
        }
    }
}
