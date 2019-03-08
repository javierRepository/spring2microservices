package com.example.microServiceCaller.controller;


import com.example.microServiceCaller.domainobject.CurrencyConversionBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by javier.montero on 01/03/2019.
 */


@RestController
public class CurrencyConversionController {

    private static final Logger LOGGER = Logger.getLogger(CurrencyConversionController.class);

    private final Environment environment;

    private StringBuilder completeUrlService;


    @Autowired
    public CurrencyConversionController(Environment environment) {
        this.environment = environment;
        this.completeUrlService = new StringBuilder();
    }

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {


        String hostService = environment.getProperty("hostService");
        String serviceCall = environment.getProperty("serviceCall");
        completeUrlService.append(hostService).append(serviceCall);


        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(completeUrlService.toString(), CurrencyConversionBean.class, uriVariables);

        CurrencyConversionBean responseCurrency = responseEntity.getBody();

        return new CurrencyConversionBean(responseCurrency.getId(), from, to, responseCurrency.getConversionMultiple(), quantity, quantity.multiply(responseCurrency.getConversionMultiple()), responseCurrency.getPort());

    }

}
