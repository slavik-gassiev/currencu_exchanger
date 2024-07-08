package com.slava.learn.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slava.learn.currency_exchanger.DTO.ExchangeRequestDTO;
import com.slava.learn.currency_exchanger.exeptions.InvalidParameterException;
import com.slava.learn.currency_exchanger.service.ExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService = new ExchangeService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (amount == null || amount.isBlank()) {
            throw new InvalidParameterException("Missing parameter - amount");
        }

        ExchangeRequestDTO exchangeRequestDTO =
                new ExchangeRequestDTO(baseCurrencyCode, targetCurrencyCode, convertToNumber(amount));
    }

    private BigDecimal convertToNumber(String amount) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(amount));
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Parameter amount must be non-negative number");
        }
    }
}
