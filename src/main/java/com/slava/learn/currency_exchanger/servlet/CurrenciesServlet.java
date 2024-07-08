package com.slava.learn.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slava.learn.currency_exchanger.DAO.CurrencyDAO;
import com.slava.learn.currency_exchanger.DAO.JdbcCurrencyDAO;
import com.slava.learn.currency_exchanger.DTO.CurrencyRequestDTO;
import com.slava.learn.currency_exchanger.Utils.MappingUtils;
import com.slava.learn.currency_exchanger.Utils.ValidationUtils;
import com.slava.learn.currency_exchanger.entity.Currency;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyDAO currencyDAO = new JdbcCurrencyDAO();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Currency> currencies = currencyDAO.findAll();
        List<CurrencyRequestDTO> currenciesDTO = currencies.stream()
                .map(MappingUtils::convertToDTO)
                .collect(Collectors.toList());

        objectMapper.writeValue(resp.getWriter(), currenciesDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        CurrencyRequestDTO  currencyRequestDTO = new CurrencyRequestDTO(name, code, sign);
        ValidationUtils.validate(currencyRequestDTO);

        Currency currency = currencyDAO.save(MappingUtils.convertToEntity(currencyRequestDTO));

        resp.setStatus(SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), MappingUtils.convertToDTO(currency));
    }
}
