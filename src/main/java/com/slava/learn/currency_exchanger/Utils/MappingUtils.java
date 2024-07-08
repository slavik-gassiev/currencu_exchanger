package com.slava.learn.currency_exchanger.Utils;
import com.slava.learn.currency_exchanger.DTO.CurrencyRequestDTO;
import com.slava.learn.currency_exchanger.DTO.CurrencyResponseDTO;
import com.slava.learn.currency_exchanger.DTO.ExchangeCurrencyResponseDTO;
import com.slava.learn.currency_exchanger.DTO.ExchangeResponseDTO;
import com.slava.learn.currency_exchanger.entity.Currency;
import com.slava.learn.currency_exchanger.entity.ExchangeCurrency;
import org.modelmapper.ModelMapper;

public class MappingUtils {
    private static ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();
        MODEL_MAPPER.typeMap(CurrencyRequestDTO.class, Currency.class)
                .addMapping(CurrencyRequestDTO::getName, Currency::setFullName);
    }

    public static Currency convertToEntity(CurrencyRequestDTO currencyRequestDTO) {
        return MODEL_MAPPER.map(currencyRequestDTO, Currency.class);
    }

    public static CurrencyResponseDTO convertToDTO(Currency currency) {
        return MODEL_MAPPER.map(currency, CurrencyResponseDTO.class);
    }

    public static ExchangeCurrencyResponseDTO convertToDTO(ExchangeCurrency exchangeCurrency) {
        return MODEL_MAPPER.map(exchangeCurrency, ExchangeCurrencyResponseDTO.class);
    }


}
