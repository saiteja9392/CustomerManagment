package com.demo.service;

import com.demo.entity.PromoCode;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.PromoCodeRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromoCodeService {

    @Autowired
    PromoCodeRepo promoCodeRepo;

    public PromoCode addPromoCode(PromoCode promoCode) {

        Optional<PromoCode> promoById = promoCodeRepo.findById(promoCode.getCode());

        if(promoById.isPresent())
            throw new ResourceException("Promo Code Already Exists");

        return promoCodeRepo.save(promoCode);
    }

    public Response enablePromoCode(String code) {

        Optional<PromoCode> promoById = promoCodeRepo.findById(code);

        if(!promoById.isPresent())
            throw new ResourceException("Promo Code Does Not Exists");

        promoById.get().setStatus(true);

        return Response.buildResponse("Promo Code Has Been Enabled",promoCodeRepo.save(promoById.get()));

    }

    public Response disablePromoCode(String code) {

        Optional<PromoCode> promoById = promoCodeRepo.findById(code);

        if(!promoById.isPresent())
            throw new ResourceException("Promo Code Does Not Exists");

        promoById.get().setStatus(false);

        return Response.buildResponse("Promo Code Has Been Disabled",promoCodeRepo.save(promoById.get()));
    }
}
