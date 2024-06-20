package com.treasury.treasuryhub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treasury.treasuryhub.model.Stock;
import com.treasury.treasuryhub.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String apiKey = "iQ9IE0esp1r6ONQNVvNwLf6m9HnhBlvU";

    public void updateStocks() {
        String url = "https://financialmodelingprep.com/api/v3/stock/list?apikey=" + apiKey;
        String json = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, Object>> stockMap = mapper.readValue(json, List.class);
            for(Map<String, Object> stockJson : stockMap) {
                String symbol = (String) stockJson.get("symbol");
                String name = (String) stockJson.get("name");
//                Double price = (Double) stockJson.get("price");
                Object priceObject = stockJson.get("price");
                double price;
                if(priceObject != null) price = Double.parseDouble(priceObject.toString());
                else {
                    price = 0;
                }
                String exchange = (String) stockJson.get("exchange");
                String exchangeShortName = (String) stockJson.get("exchangeShortName");
                String type = (String) stockJson.get("type");

                stockRepository.findBySymbol(symbol)
                        .map(newStock -> {
                            newStock.setName(name);
                            newStock.setPrice(price);
                            newStock.setExchange(exchange);
                            newStock.setExchangeShortName(exchangeShortName);
                            newStock.setType(type);
                            return stockRepository.save(newStock);
                        })
                        .orElseGet(() -> {
                            Stock newStock = new Stock();
                            newStock.setSymbol(symbol);
                            newStock.setName(name);
                            newStock.setPrice(price);
                            newStock.setExchange(exchange);
                            newStock.setExchangeShortName(exchangeShortName);
                            newStock.setType(type);
                            return stockRepository.save(newStock);
                        });
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public List<Stock> getStocksByQuery(String query) {
        return stockRepository.getStocksByQuery(query.toLowerCase());
    }

}
