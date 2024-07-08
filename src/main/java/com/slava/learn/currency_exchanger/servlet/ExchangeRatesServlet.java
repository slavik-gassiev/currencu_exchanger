package com.slava.learn.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slava.learn.currency_exchanger.DAO.ExchangeCurrencyDAO;
import com.slava.learn.currency_exchanger.DAO.JdbcExchangeCurrencyDAO;
import com.slava.learn.currency_exchanger.DTO.ExchangeCurrencyRequestDTO;
import com.slava.learn.currency_exchanger.DTO.ExchangeCurrencyResponseDTO;
import com.slava.learn.currency_exchanger.Utils.MappingUtils;
import com.slava.learn.currency_exchanger.Utils.ValidationUtils;
import com.slava.learn.currency_exchanger.entity.ExchangeCurrency;
import com.slava.learn.currency_exchanger.exeptions.InvalidParameterException;
import com.slava.learn.currency_exchanger.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeCurrencyDAO exchangeDAO = new JdbcExchangeCurrencyDAO();
    private final ExchangeRateService serviceRate = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeCurrency> exchangeRates = exchangeDAO.findAll();
        List<ExchangeCurrencyResponseDTO> exchangeRatesDTO = exchangeRates.stream()
                .map(MappingUtils::convertToDTO)
                .collect(Collectors.toList());
        objectMapper.writeValue(resp.getWriter(), exchangeRatesDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if (rate.isBlank()) {
            throw new InvalidParameterException("Missed rate in parameters");
        }

        ExchangeCurrencyRequestDTO exchangeCurrencyRequestDTO =
                new ExchangeCurrencyRequestDTO(baseCurrencyCode, targetCurrencyCode, convertToNumber(rate));

        ValidationUtils.validate(exchangeCurrencyRequestDTO);

        ExchangeCurrency exchangeCurrency = serviceRate.save(exchangeCurrencyRequestDTO);
        resp.setStatus(SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), MappingUtils.convertToDTO(exchangeCurrency));
    }

    private static BigDecimal convertToNumber(String rate) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(rate));
        }
        catch (NumberFormatException e) {
            throw new InvalidParameterException("Parameter rate must be a number");
        }
    }
}
