package com.journal.Journal.controller;

import com.journal.Journal.entity.JournalEntry;
import com.journal.Journal.entity.User;
import com.journal.Journal.service.JournalEntryService;
import com.journal.Journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    public JournalEntryService journalEntryService;
    @Autowired
    public UserService userService;
    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUsername(userName);
        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<JournalEntry> all =  user.getJournalEntries();
        if(all!=null && !all.isEmpty())
        {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        myEntry.setDate(LocalDateTime.now());
        User user = userService.findByUsername(userName);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       journalEntryService.saveEntry(myEntry, userName);
       return new ResponseEntity<JournalEntry>(myEntry,HttpStatus.CREATED);
    }

    @PutMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry, @PathVariable String userName) {
        JournalEntry entry =  journalEntryService.updateEntryById(myId,newEntry);
        if(entry==null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entry,HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalEntryService.findEntryById(id);
        if(entry.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @DeleteMapping("/id/{userName}/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id,@PathVariable String userName){
        if(userService.findByUsername(userName)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        journalEntryService.deleteById(id,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getAllEntires() {
        List<JournalEntry> allEntries = journalEntryService.getAllEntries();
        return new ResponseEntity<>(allEntries,HttpStatus.OK);
    }
}
