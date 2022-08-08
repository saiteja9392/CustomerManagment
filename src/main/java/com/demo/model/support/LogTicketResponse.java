package com.demo.model.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogTicketResponse {

    private String ticketId;
    private String category;
    private String description;
    private Date createdOn;
    private String status;
}
