package com.demo.service;

import com.demo.entity.CustomerLogin;
import com.demo.entity.Wallet;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.WalletRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    WalletRepo walletRepo;

    public CustomerLogin addWallet(Wallet wallet) {

        Optional<CustomerLogin> customerLoginByWallet = customerLoginRepo.findById(wallet.getWalletId());

        if(!customerLoginByWallet.isPresent())
            throw new ResourceException("Customer Login Not Found, Cannot Add Wallet!!!");

        if(customerLoginByWallet.get().getWallet() != null){
            throw new ResourceException("Wallet Already Mapped To Customer Login!!!");
        }

        wallet.setStatus(true);

        wallet = walletRepo.save(wallet);

        customerLoginByWallet.get().setWallet(wallet);

        CustomerLogin customerLoginAfterWallet = customerLoginRepo.save(customerLoginByWallet.get());

        return customerLoginAfterWallet;
    }

    public Wallet addMoneyToWallet(String walletId, Integer addMoney) {

        Optional<CustomerLogin> customerLoginByWallet = customerLoginRepo.findById(walletId);

        Optional<Wallet> walletById = walletRepo.findById(walletId);

        if(!customerLoginByWallet.isPresent())
            throw new ResourceException("Customer Login Not Found, Cannot Find Wallet!!!");

        if(!walletById.get().getStatus())
            throw new ResourceException("Wallet Is Disabled!!! Please Enable And Add Money");

        int balance = walletById.get().getBalance() + addMoney;

        walletById.get().setBalance(balance);

        return walletRepo.save(walletById.get());
    }

    public Response deleteWallet(String walletId) {

        Optional<Wallet> walletById = walletRepo.findById(walletId);
        Optional<CustomerLogin> customerLoginByWalletId = customerLoginRepo.findById(walletId);

        if(customerLoginByWalletId.isPresent() && customerLoginByWalletId.get().getWallet() == null)
            throw new ResourceException("No Wallet Mapped To Customer Login");

        if(!walletById.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        customerLoginByWalletId.get().setWallet(null);

        customerLoginRepo.save(customerLoginByWalletId.get());

        walletRepo.delete(walletById.get());

        return Response.buildResponse("Wallet Has Been Deleted",null);
    }

    public Response disableWallet(String walletId) {

        Optional<Wallet> walletById = walletRepo.findById(walletId);

        if(!walletById.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        if(!walletById.get().getStatus())
            throw new ResourceException("Wallet Already Disabled!!!");

        walletById.get().setStatus(false);

        walletRepo.save(walletById.get());

        return Response.buildResponse("Wallet Has Been Disabled",null);

    }

    public Response enableWallet(String walletId) {

        Optional<Wallet> walletById = walletRepo.findById(walletId);

        if(!walletById.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        if(walletById.get().getStatus())
            throw new ResourceException("Wallet Already Enabled!!!");

        walletById.get().setStatus(true);

        walletRepo.save(walletById.get());

        return Response.buildResponse("Wallet Has Been Enabled",null);

    }

    public Wallet checkBalance(String walletId) {

        Optional<Wallet> walletById = walletRepo.findById(walletId);

        if(!walletById.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        return walletById.get();
    }
}
