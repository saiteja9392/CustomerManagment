package com.demo.service;

import com.demo.entity.Offer;
import com.demo.entity.Product;
import com.demo.exception.custom.ResourceException;
import com.demo.model.OfferRequest;
import com.demo.repository.OfferRepo;
import com.demo.repository.ProductRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    OfferRepo offerRepo;

    @Autowired
    ProductRepo productRepo;

    private String Status = null;

    public Offer addOffer(Offer offer) {

        Optional<Offer> findOffer = offerRepo.findById(offer.getOfferId());

        Offer offerSaved = null;

        if(!findOffer.isPresent()) {
            offerSaved = offerRepo.save(offer);

        } else if(findOffer.get().getPercentage() == offer.getPercentage())
            throw new ResourceException(String.format("Offer Percentage Already Exists, You Can Use %s !!!",
                    findOffer.get().getOfferId()));
        else if(findOffer.isPresent())
            throw new ResourceException("Offer Id Already In Use!!!");

        return offerSaved;
    }

    public Response addOfferToProduct(OfferRequest offerRequest) {

        Optional<Product> findProduct = productRepo.findById(offerRequest.getProductId());
        Optional<Offer> findOffer = offerRepo.findById(offerRequest.getOfferId());

        if(!findProduct.isPresent())
            throw new ResourceException("Product Not Found!!!");

        if(!findOffer.isPresent())
            throw new ResourceException("Offer Not Found!!!");

        if(findProduct.get().getOffer() != null)
            throw new ResourceException("An Offer Is Already Mapped To Product!!!");

        findProduct.get().setOffer(findOffer.get());

        Product savedProduct = productRepo.save(findProduct.get());

        return Response.buildResponse("Offer Has Been Mapped To Product",savedProduct);
    }

    public Response removeOfferForProduct(String productId) {

        Optional<Product> findProduct = productRepo.findById(productId);

        if(!findProduct.isPresent())
            throw new ResourceException("Product Not Found!!!");

        if(findProduct.get().getOffer() == null)
            throw new ResourceException("No Offer Is Mapped To Product!!!");

        findProduct.get().setOffer(null);

        Product savedProduct = productRepo.save(findProduct.get());

        return Response.buildResponse("Offer Has Been Removed For Product",savedProduct);

    }

    @Transactional
    public Response deleteOffer(String offerId) {

        Optional<Offer> offerById = offerRepo.findById(offerId);

        if(!offerById.isPresent())
            throw new ResourceException("Offer Not Found");

        List<Product> productsByOffer = productRepo.findByOffer(offerId);

        productsByOffer.forEach(product -> {

            product.setOffer(null);
            productRepo.save(product);
        });

        offerRepo.delete(offerById.get());

        Response response = Response.buildResponse("Offer Deleted",offerById.get());

        return response;
    }
}
