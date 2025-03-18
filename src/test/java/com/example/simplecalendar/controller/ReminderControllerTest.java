package com.example.simplecalendar.controller;

import com.example.simplecalendar.model.Reminder;
import com.example.simplecalendar.repo.ReminderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// (Ignore deprecation warnings for now)
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReminderController.class)
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // We'll mock the repository so no real DB is involved
    private ReminderRepository reminderRepository;

    @Test
    void testGetAllReminders() throws Exception {
        // given
        Reminder r1 = new Reminder();
        r1.setId(1L);
        r1.setTitle("Title 1");
        r1.setDescription("Desc 1");

        // Stub repository
        given(reminderRepository.findAll()).willReturn(Collections.singletonList(r1));

        // when & then
        mockMvc.perform(get("/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[0].description").value("Desc 1"));
    }

    @Test
    void testCreateReminder() throws Exception {
        // given
        Reminder newReminder = new Reminder();
        newReminder.setTitle("Title 2");
        newReminder.setDescription("Desc 2");

        // Typically, the repository sets the ID when saved.
        Reminder savedReminder = new Reminder();
        savedReminder.setId(100L);
        savedReminder.setTitle("Title 2");
        savedReminder.setDescription("Desc 2");

        // Stub the save method
        given(reminderRepository.save(any(Reminder.class))).willReturn(savedReminder);

        // when & then
        mockMvc.perform(post("/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newReminder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.title").value("Title 2"))
                .andExpect(jsonPath("$.description").value("Desc 2"));
    }

    @Test
    void testDeleteReminder() throws Exception {
        // given
        Long reminderId = 5L;
        Reminder existing = new Reminder();
        existing.setId(reminderId);
        existing.setTitle("To be deleted");

        // Stub findById so the controller won't throw "Reminder not found"
        given(reminderRepository.findById(reminderId)).willReturn(Optional.of(existing));

        // when & then
        mockMvc.perform(delete("/reminders/{id}", reminderId))
                .andExpect(status().isOk());

        // Verify the delete call was made
        verify(reminderRepository, times(1)).deleteById(reminderId);
    }

    @Test
    void testUpdateReminder() throws Exception {
        // given
        Long reminderId = 10L;
        Reminder existing = new Reminder();
        existing.setId(reminderId);
        existing.setTitle("Old Title");
        existing.setDescription("Old Desc");
        existing.setStartTime(LocalDateTime.of(2025, 1, 1, 9, 0));
        existing.setEndTime(LocalDateTime.of(2025, 1, 1, 10, 0));

        // The new data to update with
        Reminder updateDetails = new Reminder();
        updateDetails.setTitle("New Title");
        updateDetails.setDescription("New Desc");
        updateDetails.setStartTime(LocalDateTime.of(2025, 1, 2, 8, 0));
        updateDetails.setEndTime(LocalDateTime.of(2025, 1, 2, 9, 0));

        // Stub findById so we get the existing reminder
        given(reminderRepository.findById(reminderId)).willReturn(Optional.of(existing));

        // Stub save to return the "updated" reminder (the controller merges fields then saves)
        given(reminderRepository.save(any(Reminder.class))).willAnswer(invocation -> {
            Reminder arg = invocation.getArgument(0);
            // Pretend the DB updated it and returned it
            return arg;
        });

        // when & then
        mockMvc.perform(put("/reminders/{id}", reminderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Desc"))
                .andExpect(jsonPath("$.startTime").value("2025-01-02 08:00:00"))
                .andExpect(jsonPath("$.endTime").value("2025-01-02 09:00:00"));

        // Also verify repository calls if you like:
        verify(reminderRepository).findById(reminderId);
        verify(reminderRepository).save(any(Reminder.class));
    }

    // Helper to convert objects to JSON string
    private String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Register Java 8 date/time module
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            // Disable writing dates as timestamps for a more readable format
            mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
