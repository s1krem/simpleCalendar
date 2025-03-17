package com.example.simplecalendar.controller;

import com.example.simplecalendar.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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
        List<Object> mockHolidays = Arrays.asList(new Object(), new Object());
        given(holidayService.getLithuanianHolidays()).willReturn(mockHolidays);

        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
