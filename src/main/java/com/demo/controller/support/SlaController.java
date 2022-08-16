package com.demo.controller.support;

import com.demo.response.Response;
import com.demo.service.support.SlaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Sla")
public class SlaController {

    @Autowired
    SlaService slaService;

    @GetMapping("/GetTicketsMissedSLA")
    public ResponseEntity<Response> getTicketsMissedSLA(@RequestParam String priority){

        List<List<String>> ticketsMissedSLA = slaService.getTicketsMissedSLA(priority);

        List<String> slaMissed = ticketsMissedSLA.stream().flatMap(missedList -> missedList.stream()).collect(Collectors.toList());

        List<CollectionModel> models = new ArrayList<>();

        slaMissed.forEach(ticket -> {

            CollectionModel<String> model = CollectionModel.of(Collections.singleton(ticket));
            WebMvcLinkBuilder linkToViewTicket = linkTo(methodOn(SupportController.class).viewTicket(ticket));
            model.add(linkToViewTicket.withRel("view-ticket"));

            models.add(model);
        });

        return new ResponseEntity(Response.buildResponse("List Of Tickets Missed SLA",models), HttpStatus.OK);
    }

    @GetMapping("/GetTicketsMissedSLAFromAndTo/{loginId}")
    public ResponseEntity<Response> getTicketsMissedSLAFromAndTo(@PathVariable String loginId,@RequestParam String from
            ,@RequestParam String to) throws ParseException {

        List<List<String>> ticketsMissedSLA = slaService.getTicketsMissedSLAFromAndTo(loginId,from,to);

        List<String> slaMissed = ticketsMissedSLA.stream().flatMap(missedList -> missedList.stream()).collect(Collectors.toList());

        List<CollectionModel> models = new ArrayList<>();

        slaMissed.forEach(ticket -> {

            CollectionModel<String> model = CollectionModel.of(Collections.singleton(ticket));
            WebMvcLinkBuilder linkToViewTicket = linkTo(methodOn(SupportController.class).viewTicket(ticket));
            model.add(linkToViewTicket.withRel("view-ticket"));

            models.add(model);
        });

        return new ResponseEntity(Response.buildResponse("List Of Tickets Missed SLA",models), HttpStatus.OK);
    }
}
