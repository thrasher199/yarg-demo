package com.example.yargtest.service;

import com.example.yargtest.model.Customer;
import com.example.yargtest.model.CustomerLoan;
import com.example.yargtest.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyReportServiceTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MyReportService reportService;


    void initData(int i) {
        Customer customer = new Customer();
        customer.setId((long) i);
        customer.setCustomerName("Abu" + i);
        customer.setAddress("Abu Gracia" + i);

        CustomerLoan customerLoanHP = new CustomerLoan();
        customerLoanHP.setLoanType("HR" + i);
        customerLoanHP.setLoanAmount(new BigDecimal(100000).add(BigDecimal.valueOf(i)));
        customerLoanHP.setCustomer(customer);

        CustomerLoan customerLoanM = new CustomerLoan();
        customerLoanM.setLoanType("Mortage" + i);
        customerLoanM.setLoanAmount(new BigDecimal(1000000).add(BigDecimal.valueOf(i)));
        customerLoanM.setCustomer(customer);

        CustomerLoan customerLoanP = new CustomerLoan();
        customerLoanP.setLoanType("Personal" + i);
        customerLoanP.setLoanAmount(new BigDecimal(10000).add(BigDecimal.valueOf(i)));
        customerLoanP.setCustomer(customer);

        Set<CustomerLoan> customerLoanSet = new LinkedHashSet<>();
        customerLoanSet.add(customerLoanHP);
        customerLoanSet.add(customerLoanM);
        customerLoanSet.add(customerLoanP);

        customer.setCustomerLoans(customerLoanSet);
        customerRepository.save(customer);
    }

    @Test
    void generateReport() throws IOException {
        int size = 100;
        for (int i = 1; i <= size; i++) {
            initData(i);
        }
        System.out.println("Done insert");

        long start = System.currentTimeMillis();
        for (int i = 1; i <= size; i++) {
            reportService.generateReport(i);
        }

        long end = System.currentTimeMillis();
        long execution  = end - start;
        System.out.println("execution in " + execution  + " millisecond");
    }
}