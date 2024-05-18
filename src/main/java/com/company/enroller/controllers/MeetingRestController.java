package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
    @RequestMapping("/meetings")
    public class MeetingRestController {

        @Autowired
        MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

        @RequestMapping(value = "", method = RequestMethod.GET)
        public ResponseEntity<?> getMeetings() {
		 Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
        meetingService.addMeeting(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meetingService.updateMeeting(id, updatedMeeting);
        return new ResponseEntity<>(updatedMeeting, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meetingService.deleteMeeting(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody Participant participant) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Participant existingParticipant = participantService.findByLogin(participant.getLogin());
        if (existingParticipant == null) {
            return new ResponseEntity<>("Użytkownik o podanym loginie nie istnieje.", HttpStatus.BAD_REQUEST);
        }

        if (meeting.getParticipants().contains(existingParticipant)) {
            return new ResponseEntity<>("Użytkownik już uczestniczy w spotkaniu.", HttpStatus.BAD_REQUEST);
        }

        meeting.addParticipant(existingParticipant);
        meetingService.updateMeeting(id, meeting);
        return new ResponseEntity<>(existingParticipant, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/participants/{login}")
    public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable("id") long id, @PathVariable("login") String login) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Participant participantToRemove = null;
        for (Participant participant : meeting.getParticipants()) {
            if (participant.getLogin().equals(login)) {
                participantToRemove = participant;
                break;
            }
        }
        if (participantToRemove == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meeting.removeParticipant(participantToRemove);
        meetingService.updateMeeting(id, meeting);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting.getParticipants(), HttpStatus.OK);
    }

    }

