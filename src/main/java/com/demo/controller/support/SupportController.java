package com.demo.controller.support;

import com.demo.model.support.LogTicket;
import com.demo.model.support.LogTicketResponse;
import com.demo.model.support.TicketUpdate;
import com.demo.response.Response;
import com.demo.service.support.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Support")
public class SupportController {

    @Autowired
    SupportService supportService;

    @PostMapping("/LogTicket")
    public EntityModel<Response> logTicket(@Valid @RequestBody LogTicket logTicket){

        Response loggedTicket = supportService.logTicket(logTicket);

        LogTicketResponse logTicketResponse = (LogTicketResponse) loggedTicket.getEntity();

        EntityModel<Response> model = EntityModel.of(loggedTicket);
        WebMvcLinkBuilder linkToViewTicket = linkTo(methodOn(this.getClass()).viewTicket(logTicketResponse.getTicketId()));
        model.add(linkToViewTicket.withRel("view-ticket"));

        return model;
    }

    @PutMapping("/UpdateTicket")
    public EntityModel<Response> updateTicket(@Valid @RequestBody TicketUpdate ticketUpdate){

        Response updateTicketResponse = supportService.updateTicket(ticketUpdate);

        EntityModel<Response> model = EntityModel.of(updateTicketResponse);
        WebMvcLinkBuilder linkToViewTicket = linkTo(methodOn(this.getClass()).viewTicket(ticketUpdate.getTicketId()));
        model.add(linkToViewTicket.withRel("view-ticket"));

        return model;
    }

    @PostMapping("/ResolveTicket")
    public EntityModel<Response> resolveTicket(@Valid @RequestBody TicketUpdate ticketUpdate){

        Response resolveTicketResponse = supportService.resolveTicket(ticketUpdate);

        EntityModel<Response> model = EntityModel.of(resolveTicketResponse);
        WebMvcLinkBuilder linkToViewTicket = linkTo(methodOn(this.getClass()).viewTicket(ticketUpdate.getTicketId()));
        model.add(linkToViewTicket.withRel("view-ticket"));

        return model;
    }

    @GetMapping("/ViewTicket/{ticketId}")
    public ResponseEntity<Response> viewTicket(@PathVariable String ticketId){

        return new ResponseEntity<>(supportService.viewTicket(ticketId), HttpStatus.OK);
    }

    @GetMapping("/GetAllTicketsByStatus")
    public ResponseEntity<Response> getAllTicketsByStatus(@RequestParam Boolean status){

        return new ResponseEntity<>(supportService.getAllTicketsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/GetAllTicketsOfCustomer/{loginId}")
    public ResponseEntity<Response> getAllTicketsOfCustomer(@PathVariable String loginId){

        return new ResponseEntity<>(supportService.getAllTicketsOfCustomer(loginId), HttpStatus.OK);
    }

    @DeleteMapping("/DeleteTicket/{ticketId}")
    public ResponseEntity<Response> deleteTicket(@PathVariable String ticketId){

        return new ResponseEntity<>(supportService.deleteTicket(ticketId), HttpStatus.OK);
    }

    @PutMapping("/ChangePriorityOfTicket")
    public ResponseEntity<Response> changePriorityOfTicket(@RequestParam String ticketId, @RequestParam String priority){

        return new ResponseEntity<>(supportService.changePriorityOfTicket(ticketId,priority), HttpStatus.OK);
    }

    @GetMapping("/GetAllTicketsByCategory")
    public ResponseEntity<Response> getAllTicketsByCategory(){

        /* TODO - Need to update logic */
        return new ResponseEntity<>(supportService.getAllTicketsByCategory(), HttpStatus.OK);
    }
}
