package com.demo.entity.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Support {

    @Id
    @NotNull
    private String ticketId;

    private String loginId;

    @Column(name = "created")
    private Date createdOn;

    @Column(name = "resolved")
    private Date resolvedOn;

    @Lob
    private String description;

    private String category;

    private String status;

    private String priority;

    private Long sla;

    @OneToMany(mappedBy = "support",cascade = CascadeType.ALL)
    private List<TicketNote> ticketNotes = new ArrayList<>();

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        ticketId = "CMINC"+dtf.format(now);

        this.ticketId = ticketId;
    }
}
