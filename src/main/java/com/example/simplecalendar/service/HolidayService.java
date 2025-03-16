package com.example.simplecalendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HolidayService {
    private static final String HOLIDAY_URL = "https://date.nager.at/api/v3/PublicHolidays/2025/LT";

    private final RestTemplate restTemplate;

    @Autowired
    public HolidayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Object> getLithuanianHolidays(){
        try{
            return restTemplate.getForObject(HOLIDAY_URL, List.class);
        } catch (Exception e){
            throw new RuntimeException("Error fetching holidays", e);
        }
    }
}
