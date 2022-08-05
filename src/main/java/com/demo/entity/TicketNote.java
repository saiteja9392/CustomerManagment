package com.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TicketNote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer serialNumber;

    @Lob
    private String notes;

    private Date notesUpdatedDate;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "support_ticket_id", nullable = false)
    private Support support;

    public void setSupport(Support support) {
        this.support = support;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getNotesUpdatedDate() {
        return notesUpdatedDate;
    }

    public void setNotesUpdatedDate(Date notesUpdatedDate) {
        this.notesUpdatedDate = notesUpdatedDate;
    }
}
