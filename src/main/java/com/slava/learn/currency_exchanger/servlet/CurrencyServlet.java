package com.slava.learn.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slava.learn.currency_exchanger.DAO.CurrencyDAO;
import com.slava.learn.currency_exchanger.DAO.JdbcCurrencyDAO;
import com.slava.learn.currency_exchanger.Utils.ValidationUtils;
import com.slava.learn.currency_exchanger.entity.Currency;
import com.slava.learn.currency_exchanger.exeptions.NotFoundException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

import static com.slava.learn.currency_exchanger.Utils.MappingUtils.convertToDTO;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDAO currencyDAO = new JdbcCurrencyDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getPathInfo().replaceFirst("/", "");
        ValidationUtils.validateCurrencyCode(code);

        Currency currency = currencyDAO.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency with '" + code + "' not found"));

        objectMapper.writeValue(resp.getWriter(), convertToDTO(currency));
    }
}
