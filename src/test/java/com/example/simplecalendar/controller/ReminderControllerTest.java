package com.example.simplecalendar.controller;

import com.example.simplecalendar.model.Reminder;
import com.example.simplecalendar.repo.ReminderRepository;
import com.example.simplecalendar.service.HolidayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderRepository reminderRepository;

    @BeforeEach
    void setUp() {
        // Clear DB or set up initial data
        reminderRepository.deleteAll();
    }

    @Test
    void testGetAllReminders() throws Exception {
        // given
        Reminder r1 = new Reminder();
        r1.setTitle("Title 1");
        r1.setDescription("Desc 1");
        reminderRepository.save(r1);

        // when & then
        mockMvc.perform(get("/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[0].description").value("Desc 1"));
    }

    @Test
    void testCreateReminder() throws Exception {
        // given
        Reminder newReminder = new Reminder();
        newReminder.setTitle("Title 2");
        newReminder.setDescription("Desc 2");

        // when & then
        mockMvc.perform(post("/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newReminder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Title 2"))
                .andExpect(jsonPath("$.description").value("Desc 2"));
    }

    // etc.

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
