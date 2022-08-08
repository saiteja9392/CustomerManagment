package com.demo.repository.support;

import com.demo.entity.support.TicketNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketNoteRepo extends JpaRepository<TicketNote,Integer> {

}
