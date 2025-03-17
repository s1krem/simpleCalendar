package com.example.simplecalendar.repo;

import com.example.simplecalendar.model.Reminder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReminderRepositoryTest {

    @Autowired
    private ReminderRepository reminderRepository;

    @Test
    void testSaveAndFindReminder() {
        // given
        Reminder reminder = new Reminder();
        reminder.setTitle("Test Title");
        reminder.setDescription("Test Description");
        reminderRepository.save(reminder);

        // when
        Optional<Reminder> found = reminderRepository.findById(reminder.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Title");
        assertThat(found.get().getDescription()).isEqualTo("Test Description");
    }
}
