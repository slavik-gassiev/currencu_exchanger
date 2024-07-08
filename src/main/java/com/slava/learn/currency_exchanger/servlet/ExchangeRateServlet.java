package com.slava.learn.currency_exchanger.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slava.learn.currency_exchanger.DAO.ExchangeCurrencyDAO;
import com.slava.learn.currency_exchanger.DAO.JdbcExchangeCurrencyDAO;
import com.slava.learn.currency_exchanger.DTO.CurrencyRequestDTO;
import com.slava.learn.currency_exchanger.DTO.ExchangeCurrencyRequestDTO;
import com.slava.learn.currency_exchanger.Utils.MappingUtils;
import com.slava.learn.currency_exchanger.Utils.ValidationUtils;
import com.slava.learn.currency_exchanger.entity.ExchangeCurrency;
import com.slava.learn.currency_exchanger.exeptions.DatabaseOperationException;
import com.slava.learn.currency_exchanger.exeptions.InvalidParameterException;
import com.slava.learn.currency_exchanger.exeptions.NotFoundException;
import com.slava.learn.currency_exchanger.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeCurrencyDAO exchangeDAO = new JdbcExchangeCurrencyDAO();
    private final ExchangeRateService exchangeService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if(req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, res);
        } else {
            super.service(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currencyCodes = req.getPathInfo().replaceFirst("/", "");

        if(currencyCodes.length() != 6) {
            throw new InvalidParameterException("Currency codes are either not provided or provided in an incorrect format");
        }

        String baseCurrencyCode = currencyCodes.substring(0, 3);
        String targetCurrencyCode = currencyCodes.substring(3);

        ValidationUtils.validateCurrencyCode(baseCurrencyCode);
        ValidationUtils.validateCurrencyCode(targetCurrencyCode);

        ExchangeCurrency exchangeCurrency = exchangeDAO.findByCodes(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException(
                        "Excange rate '" + baseCurrencyCode + "' - '" + targetCurrencyCode + "' non found")
                );

           objectMapper.writeValue(resp.getWriter(), MappingUtils.convertToDTO(exchangeCurrency));
    }


    protected void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String currencyCodes = req.getPathInfo().replaceFirst("/", "");
        if (currencyCodes.length() != 6) {
            throw new InvalidParameterException("Currency codes are either not provided or provided in an incorrect format");
        }

        String baseCurrencyCode = currencyCodes.substring(0, 3);
        String targetCurrencyCode = currencyCodes.substring(3);

        ValidationUtils.validateCurrencyCode(baseCurrencyCode);
        ValidationUtils.validateCurrencyCode(targetCurrencyCode);

        String parameter = req.getReader().readLine();
        if (parameter == null || !parameter.contains("rate")) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        String rate = parameter.replace("rate=", "");
        if (rate.isBlank()) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        ExchangeCurrencyRequestDTO exchangeCurrencyRequestDTO =
                new ExchangeCurrencyRequestDTO(baseCurrencyCode, targetCurrencyCode, convertToNumber(rate));
        ExchangeCurrency exchangeCurrency = exchangeService.update(exchangeCurrencyRequestDTO);
        objectMapper.writeValue(res.getWriter(), MappingUtils.convertToDTO(exchangeCurrency));

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
