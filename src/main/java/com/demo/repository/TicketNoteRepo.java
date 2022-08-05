package com.demo.repository;

import com.demo.entity.TicketNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketNoteRepo extends JpaRepository<TicketNote,Integer> {

}
