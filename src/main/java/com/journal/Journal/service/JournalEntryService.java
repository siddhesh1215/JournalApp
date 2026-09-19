package com.journal.Journal.service;

import com.journal.Journal.entity.JournalEntry;
import com.journal.Journal.entity.User;
import com.journal.Journal.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    //private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUsername(userName);
        JournalEntry saved =  journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(journalEntry);
        userService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findEntryById(ObjectId id) {
        return journalEntryRepository.findById(id.toHexString());
    }

    public JournalEntry updateEntryById(ObjectId id, JournalEntry newEntry) {
        JournalEntry old = journalEntryRepository.findById(id.toHexString()).orElse(null);
        if(old != null){
            old.setTitle(newEntry.getTitle().equals(old.getTitle()) ? old.getTitle() : newEntry.getTitle());
            old.setContent(newEntry.getContent().equals(old.getContent()) ? old.getContent() : newEntry.getContent());
            saveEntry(old);
            return old;
        }
        return null;
    }

    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUsername(userName);
        user.getJournalEntries().removeIf(x-> x.getId().equals(id));
        userService.saveUser(user);
        journalEntryRepository.deleteById(id.toHexString());
    }
}
