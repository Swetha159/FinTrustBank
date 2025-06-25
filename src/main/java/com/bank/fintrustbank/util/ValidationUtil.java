package com.bank.fintrustbank.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationUtil{

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\d{10}$");
    }

    public static boolean isValidAadhar(String aadhar) {
        return aadhar != null && aadhar.matches("^\\d{12}$");
    }

    public static boolean isValidPan(String pan) {
        return pan != null && pan.matches("^[A-Z]{5}[0-9]{4}[A-Z]$");
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    public static boolean isValidAccountNo(Long acc) {
        return acc != null && String.valueOf(acc).matches("^\\d{12}$");
    }

    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Strict parsing

        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidDateRange(String startDate, String endDate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            return !end.before(start);
        } catch (ParseException e) {
            return false;
        }
    }


    
    // You can add more validators here (e.g., date, gender, etc.)
}
