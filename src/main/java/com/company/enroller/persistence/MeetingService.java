package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

	public Meeting findByID(Long id) {
		return session.getSession().get(Meeting.class, id);
	}

	public void addMeeting(Meeting meeting) {
		Transaction transaction = session.beginTransaction();
		session.save(meeting);
		transaction.commit();
	}

	public void updateMeeting(Long id, Meeting updatedMeeting) {
		Transaction transaction = session.beginTransaction();
		Meeting meeting = session.get(Meeting.class, id);
		meeting.setTitle(updatedMeeting.getTitle());
		meeting.setDescription(updatedMeeting.getDescription());
		meeting.setDate(updatedMeeting.getDate());
		session.update(meeting);
		transaction.commit();
	}

	public void deleteMeeting(Long id) {
		Transaction transaction = session.beginTransaction();
		Meeting meeting = session.get(Meeting.class, id);
		session.delete(meeting);
		transaction.commit();
	}
}
