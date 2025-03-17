package com.example.simplecalendar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HolidayServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        holidayService = new HolidayService(restTemplate);
    }

    @Test
    void getLithuanianHolidays_ShouldReturnList_WhenApiCallSuccessful() {
        // given
        List<Object> mockResponse = Collections.singletonList(new Object());
        when(restTemplate.getForObject(
                eq("https://date.nager.at/api/v3/PublicHolidays/2025/LT"),
                eq(List.class))
        ).thenReturn(mockResponse);

        // when
        List<Object> result = holidayService.getLithuanianHolidays();

        // then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void getLithuanianHolidays_ShouldThrowRuntimeException_WhenApiCallFails() {
        // given
        when(restTemplate.getForObject(
                eq("https://date.nager.at/api/v3/PublicHolidays/2025/LT"),
                eq(List.class))
        ).thenThrow(new RuntimeException("API Error"));

        // when & then
        assertThrows(RuntimeException.class, () -> holidayService.getLithuanianHolidays());
    }
}
