package com.slava.learn.currency_exchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {
    Long id;
    String code;
    String full_name;
    String sign;

    public CurrencyDTO(String code, String full_name, String sign) {
        this.code = code;
        this.full_name = full_name;
        this.sign = sign;
    }

}
