package com.example.simplecalendar.controller;

import com.example.simplecalendar.model.Reminder;
import com.example.simplecalendar.repo.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    @Autowired
    private ReminderRepository reminderRepository;

    @GetMapping
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @PostMapping
    public Reminder createReminder(@RequestBody Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable Long id, @RequestBody Reminder reminderDetails) {
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new RuntimeException("Reminder not found with id :: " + id));
        reminder.setTitle(reminderDetails.getTitle());
        reminder.setDescription(reminderDetails.getDescription());
        reminder.setStartTime(reminderDetails.getStartTime());
        reminder.setRecurrence(reminderDetails.getRecurrence());
        reminder.setRecurrenceEndTime(reminderDetails.getRecurrenceEndTime());
        reminderRepository.save(reminder);
        return ResponseEntity.ok(reminder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Reminder> deleteReminder(@PathVariable Long id) {
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new RuntimeException("Reminder not found with id :: " + id));
        reminderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
