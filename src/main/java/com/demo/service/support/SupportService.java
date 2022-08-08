package com.demo.service.support;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.support.Support;
import com.demo.entity.support.TicketNote;
import com.demo.enumaration.Priority;
import com.demo.enumaration.SupportCategory;
import com.demo.exception.custom.ResourceException;
import com.demo.model.support.LogTicket;
import com.demo.model.support.LogTicketResponse;
import com.demo.model.support.TicketUpdate;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.support.SupportRepo;
import com.demo.repository.support.TicketNoteRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.demo.enumaration.Priority.P4;
import static com.demo.enumaration.Status.*;

@Service
public class SupportService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    SupportRepo supportRepo;

    @Autowired
    TicketNoteRepo ticketNoteRepo;

    public Response logTicket(LogTicket logTicket) {

        Optional<Customer> customerInfo = customerRepo.findById(logTicket.getLoginId());
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(logTicket.getLoginId());

        if(!customerInfo.isPresent()|| !customerLoginInfo.isPresent())
            throw new ResourceException("No Customer Found");

        if(logTicket.getCategory() != null)
            Arrays.stream(SupportCategory.values())
                .filter(sc -> sc.name().contentEquals(logTicket.getCategory().toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new ResourceException("In-Valid Support Category"));

        Support support = new Support();
        support.setTicketId(support.getTicketId());
        support.setLoginId(logTicket.getLoginId());

        if(logTicket.getCategory() == null)
            logTicket.setCategory("GENERAL");

        support.setCategory(logTicket.getCategory().toUpperCase());

        support.setDescription(logTicket.getDescription());
        support.setCreatedOn(new Date());
        support.setPriority(P4.name());
        support.setStatus(CREATED.name());

        Support ticketCreated = supportRepo.save(support);

        LogTicketResponse logTicketResponse = LogTicketResponse.builder()
                .ticketId(ticketCreated.getTicketId())
                .category(ticketCreated.getCategory().toUpperCase())
                .description(ticketCreated.getDescription())
                .createdOn(ticketCreated.getCreatedOn())
                .status(ticketCreated.getStatus())
                .build();

        return Response.buildResponse("Request Has Been Submitted!!!",logTicketResponse);
    }

    public Response updateTicket(TicketUpdate ticketUpdate){

        Optional<Support> ticketInfo = supportRepo.findById(ticketUpdate.getTicketId());
        List<TicketNote> ticketNotes = new ArrayList<>();

        if(!ticketInfo.isPresent())
            throw new ResourceException("In-Valid Ticket Number");

        if(ticketInfo.get().getStatus().contentEquals(RESOLVED.name()))
            throw new ResourceException("Ticket Is Closed!! Please Re-Open And Update Case Notes");

        TicketNote ticketNote = new TicketNote();

        ticketNote.setSupport(ticketInfo.get());
        ticketNote.setNotes(ticketUpdate.getNotes());
        ticketNote.setNotesUpdatedDate(new Date());

        ticketNotes.add(ticketNote);

        ticketInfo.get().setStatus(IN_PROGRESS.name());
        ticketInfo.get().setTicketNotes(ticketNotes);

        Support updatedTicketInfo = supportRepo.save(ticketInfo.get());

        return Response.buildResponse("Updated Ticket Notes",updatedTicketInfo);
    }
    public Response resolveTicket(TicketUpdate ticketUpdate) {

        Date date = new Date();
        Optional<Support> ticketInfo = supportRepo.findById(ticketUpdate.getTicketId());
        List<TicketNote> ticketNotes = new ArrayList<>();

        if(!ticketInfo.isPresent())
            throw new ResourceException("In-Valid Ticket Number");

        if(ticketInfo.get().getStatus().contentEquals(RESOLVED.name()))
            throw new ResourceException("Ticket Already Closed");

        TicketNote ticketNote = new TicketNote();

        ticketNote.setSupport(ticketInfo.get());
        ticketNote.setNotes(ticketUpdate.getNotes());
        ticketNote.setNotesUpdatedDate(new Date());

        ticketNotes.add(ticketNote);
        ticketInfo.get().setTicketNotes(ticketNotes);

        ticketInfo.get().setResolvedOn(date);

        long slaInMinutes = ((date.getTime() - ticketInfo.get().getCreatedOn().getTime()) /1000) / 60;

        ticketInfo.get().setSla(slaInMinutes);
        ticketInfo.get().setStatus(RESOLVED.name());

        return Response.buildResponse("Request Has Been Closed!!!",supportRepo.save(ticketInfo.get()));
    }

    public Response viewTicket(String ticketId){

        Optional<Support> ticketInfo = supportRepo.findById(ticketId);

        if(!ticketInfo.isPresent())
            throw new ResourceException("In-Valid Ticket Number");

        Collections.sort(ticketInfo.get().getTicketNotes(),(t1, t2) -> t2.getNotesUpdatedDate().compareTo(t1.getNotesUpdatedDate()));

        return Response.buildResponse("Request Details!!!",ticketInfo.get());
    }

    public Response getAllTicketsByStatus(Boolean status) {

        List<Support> allTickets = supportRepo.findAll();

        Response response = null;

        if(true){

            List<Support> openTickets = allTickets.stream().filter(ticket -> ticket.getStatus().contentEquals(CREATED.name())
                                                                    || ticket.getStatus().contentEquals(IN_PROGRESS.name())).collect(Collectors.toList());
            response = Response.buildResponse("Open Tickets", openTickets);
        }
        else{

            List<Support> closedTickets = allTickets.stream().filter(ticket -> ticket.getStatus().contentEquals(RESOLVED.name())).collect(Collectors.toList());
            response = Response.buildResponse("Resolved Tickets", closedTickets);
        }

        return response;
    }

    public Response getAllTicketsOfCustomer(String loginId) {

        List<Support> allTickets = supportRepo.findByLoginId(loginId);

        Collections.sort(allTickets, Comparator.comparing(Support::getCreatedOn).reversed());

        return Response.buildResponse("Tickets Details",allTickets);
    }

    public Response changePriorityOfTicket(String ticketId, String priority) {

        Optional<Support> ticketInfo = supportRepo.findById(ticketId);

        if(!ticketInfo.isPresent())
            throw new ResourceException("In-Valid Ticket Number!!!");

        Arrays.stream(Priority.values()).filter(p -> p.name().contentEquals(priority.toUpperCase()))
                .findFirst().orElseThrow(() ->new ResourceException("In-Valid Priority"));

        if(ticketInfo.get().getPriority().contentEquals(priority.toUpperCase()))
            throw new ResourceException("Ticket Has Same Priority");

        ticketInfo.get().setPriority(priority.toUpperCase());

        return Response.buildResponse("Ticket Priority Has Been Changed",supportRepo.save(ticketInfo.get()));
    }

    public Response deleteTicket(String ticketId) {

        Optional<Support> ticketInfo = supportRepo.findById(ticketId);

        if(!ticketInfo.isPresent())
            throw new ResourceException("In-Valid Ticket Number");

        supportRepo.delete(ticketInfo.get());

        return Response.buildResponse("Ticket Has Been Deleted",ticketInfo.get());
    }

    public Response getAllTicketsByCategory() {

        List<Support> allTickets = supportRepo.findAll();

        Map<String, List<Support>> groupByCategory = allTickets.stream().collect(Collectors.groupingBy(Support::getCategory, Collectors.toList()));

        return Response.buildResponse("Ticket Details Grouped By Category",groupByCategory);
    }
}
