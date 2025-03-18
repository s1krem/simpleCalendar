package com.example.simplecalendar.controller;

import com.example.simplecalendar.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HolidayController.class)
class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // again, ignoring deprecation
    @MockBean
    private HolidayService holidayService;

    @Test
    void testGetAllHolidays() throws Exception {
        // This is already a list with 2 maps
        List<Map<String, Object>> mockHolidays = Arrays.asList(
                Map.of("date", "2025-01-01", "localName", "New Year's Day"),
                Map.of("date", "2025-02-16", "localName", "Lithuanian Independence Day")
        );
        given(holidayService.getLithuanianHolidays()).willReturn(List.of(mockHolidays.toArray()));


        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                // Expect 2 items at the top level
                .andExpect(jsonPath("$.length()").value(2));
    }

}
