package com.demo.service.support;

import com.demo.entity.support.Support;
import com.demo.enumaration.Priority;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.support.SlaRepo;
import com.demo.repository.support.SupportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.demo.enumaration.Priority.*;

@Service
public class SlaService {

    @Autowired
    SlaRepo slaRepo;

    @Autowired
    SupportRepo supportRepo;

    public List<List<String>> getTicketsMissedSLA(String priority) {

        List<Support> allTicketInfo = supportRepo.getNotNullSlaTickets();

        List<List<String>> missedSLATicketIds = new ArrayList<>();

        if(!priority.isEmpty()){
            Arrays.stream(Priority.values()).filter(p -> p.name().contentEquals(priority.toUpperCase()))
                    .findFirst().orElseThrow(() ->new ResourceException("In-Valid Priority"));
        }

        List<String> listOfP1SLAMissed;
        List<String> listOfP2SLAMissed;
        List<String> listOfP3SLAMissed;
        List<String> listOfP4SLAMissed;

        if(priority.isEmpty()){

            listOfP1SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P1.name()))
                    .filter(p1 -> p1.getSla() > slaRepo.findById(P1.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            listOfP2SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P2.name()))
                    .filter(p2 -> p2.getSla() > slaRepo.findById(P2.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            listOfP3SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P3.name()))
                    .filter(p3 -> p3.getSla() > slaRepo.findById(P3.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            listOfP4SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P4.name()))
                    .filter(p4 -> p4.getSla() > slaRepo.findById(P4.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            missedSLATicketIds.add(listOfP1SLAMissed);
            missedSLATicketIds.add(listOfP2SLAMissed);
            missedSLATicketIds.add(listOfP3SLAMissed);
            missedSLATicketIds.add(listOfP4SLAMissed);
        }
        else if (P1.name().contentEquals(priority.toUpperCase())) {

            listOfP1SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P1.name()))
                    .filter(p1 -> p1.getSla() > slaRepo.findById(P1.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            missedSLATicketIds.add(listOfP1SLAMissed);
        }
        else if (P2.name().contentEquals(priority.toUpperCase())) {

            listOfP2SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P2.name()))
                    .filter(p2 -> p2.getSla() > slaRepo.findById(P2.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            missedSLATicketIds.add(listOfP2SLAMissed);
        }
        else if (P3.name().contentEquals(priority.toUpperCase())) {

            listOfP3SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P3.name()))
                    .filter(p3 -> p3.getSla() > slaRepo.findById(P3.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            missedSLATicketIds.add(listOfP3SLAMissed);
        }
        else if (P4.name().contentEquals(priority.toUpperCase())){

            listOfP4SLAMissed = allTicketInfo.stream().filter(ticket -> ticket.getPriority().contentEquals(P4.name()))
                    .filter(p4 -> p4.getSla() > slaRepo.findById(P4.name()).get().getTime())
                    .map(Support::getTicketId)
                    .collect(Collectors.toList());

            missedSLATicketIds.add(listOfP4SLAMissed);
        }

        return missedSLATicketIds;
    }

    public List<List<String>> getTicketsMissedSLAFromAndTo(String loginId, String from, String to) throws ParseException {

        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

        List<Support> ticketsBasedOnLoginId = supportRepo.getTicketsBasedOnLoginId(loginId, fromDate, toDate);

        List<String> listOfP1SLAMissed = ticketsBasedOnLoginId.stream().filter(ticket -> ticket.getPriority().contentEquals(P1.name()))
                .filter(p1 -> p1.getSla() > slaRepo.findById(P1.name()).get().getTime())
                .map(Support::getTicketId)
                .collect(Collectors.toList());
        List<String> listOfP2SLAMissed = ticketsBasedOnLoginId.stream().filter(ticket -> ticket.getPriority().contentEquals(P2.name()))
                .filter(p2 -> p2.getSla() > slaRepo.findById(P2.name()).get().getTime())
                .map(Support::getTicketId)
                .collect(Collectors.toList());
        List<String> listOfP3SLAMissed = ticketsBasedOnLoginId.stream().filter(ticket -> ticket.getPriority().contentEquals(P3.name()))
                .filter(p3 -> p3.getSla() > slaRepo.findById(P4.name()).get().getTime())
                .map(Support::getTicketId)
                .collect(Collectors.toList());
        List<String> listOfP4SLAMissed = ticketsBasedOnLoginId.stream().filter(ticket -> ticket.getPriority().contentEquals(P4.name()))
                .filter(p4 -> p4.getSla() > slaRepo.findById(P4.name()).get().getTime())
                .map(Support::getTicketId)
                .collect(Collectors.toList());

        return Arrays.asList(listOfP1SLAMissed,listOfP2SLAMissed,listOfP3SLAMissed,listOfP4SLAMissed);

    }
}
