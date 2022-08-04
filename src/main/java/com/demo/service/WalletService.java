package com.demo.service;

import com.demo.entity.CustomerLogin;
import com.demo.entity.Wallet;
import com.demo.entity.WalletTransaction;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.WalletRepo;
import com.demo.repository.WalletTransactionRepo;
import com.demo.response.Response;
import com.demo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.demo.enumaration.Status.CREDITED;

@Service
public class WalletService {

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    WalletTransactionRepo walletTransactionRepo;

    public Response addWallet(Wallet wallet) {

        Optional<CustomerLogin> customerLoginByWallet = customerLoginRepo.findById(wallet.getWalletId());

        if(!customerLoginByWallet.isPresent())
            throw new ResourceException("Customer Login Not Found, Cannot Add Wallet!!!");

        if(customerLoginByWallet.get().getWallet() != null){
            throw new ResourceException("Wallet Already Mapped To Customer Login!!!");
        }

        wallet.setStatus(true);

        wallet = walletRepo.save(wallet);

        customerLoginByWallet.get().setWallet(wallet);

        Response response = Response.buildResponse("Wallet Added",customerLoginRepo.save(customerLoginByWallet.get()));

        return response;
    }

    @Transactional
    public Response addMoneyToWallet(String walletId, Integer addMoney) {

        Optional<CustomerLogin> customerLoginByWallet = customerLoginRepo.findById(walletId);

        Optional<Wallet> walletById = walletRepo.findById(walletId);

        if(!customerLoginByWallet.isPresent())
            throw new ResourceException("Customer Login Not Found, Cannot Find Wallet!!!");

        if(!walletById.get().getStatus())
            throw new ResourceException("Wallet Is Disabled!!! Please Enable And Add Money");

        int balance = walletById.get().getBalance() + addMoney;

        walletById.get().setBalance(balance);

        Wallet updatedWallet = walletRepo.save(walletById.get());

        addToWalletTransaction(walletId,addMoney);

        Response response = Response.buildResponse("Money Added To Wallet",updatedWallet);

        return response;
    }

    @Transactional
    private WalletTransaction addToWalletTransaction(String loginId, int amount) {

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setTransactionId(walletTransaction.getTransactionId());
        walletTransaction.setTransactionType(CREDITED.name());
        walletTransaction.setAmount(amount);
        walletTransaction.setLoginId(loginId);
        walletTransaction.setReferenceId(Utils.getBalanceTransactionId());

        return walletTransactionRepo.save(walletTransaction);
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

        return Response.buildResponse("Wallet Has Been Disabled",walletRepo.save(walletById.get()));

    }

    public Response enableWallet(String walletId) {

        Optional<Wallet> walletById = walletRepo.findById(walletId);

        if(!walletById.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        if(walletById.get().getStatus())
            throw new ResourceException("Wallet Already Enabled!!!");

        walletById.get().setStatus(true);

        return Response.buildResponse("Wallet Has Been Enabled",walletRepo.save(walletById.get()));

    }

    public Response checkBalance(String walletId) {

        Optional<Wallet> walletById = walletRepo.findById(walletId);

        if(!walletById.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        return Response.buildResponse("Balance Details",walletById.get());
    }
}
