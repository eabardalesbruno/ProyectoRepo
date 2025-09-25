package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.WalletTransactionDTO;
import com.proriberaapp.ribera.Domain.dto.PaymentDetailsPromoterDTO;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WalletTransactionRepository extends R2dbcRepository<WalletTransactionEntity,Integer> {
    Mono<WalletTransactionEntity> findByOperationCode(String operationCode);

    @Query("""
        SELECT 
            COALESCE(u.firstName || ' ' || u.lastName, p.firstName || ' ' || p.lastName) AS username,
            wt.inicialdate, 
            wt.amount, 
            wt.description, 
            wt.operationcode,
            wt.transactioncategoryid
        FROM wallettransaction wt
        LEFT JOIN userclient u ON wt.walletid = u.walletid
        LEFT JOIN userpromoter p ON wt.walletid = p.walletid
        WHERE wt.walletid = :walletId
    """)
    Flux<WalletTransactionDTO> findTransactionDetailsByWalletId(Integer walletId);



    @Query("""
    SELECT DISTINCT
        uc.firstName || ' ' || uc.lastName AS clientName, 
        pb.amount AS paymentAmount,                       
        pb.description AS paymentDescription,            
        up.firstName || ' ' || up.lastName AS promoterName, 
        w.walletid AS promoterWallet,                    
        pb.paymentbookid AS paymentBookId,               
        pb.bookingid AS bookingId,                       
        b.userclientid AS clientId,                      
        b.costfinal AS totalCost 
    FROM paymentbook pb
    LEFT JOIN booking b ON pb.bookingid = b.bookingid
    LEFT JOIN userclient uc ON pb.userclientid = uc.userclientid
    LEFT JOIN userpromoter up ON b.userpromotorid = up.userpromoterid
    LEFT JOIN wallet w 
        ON (w.walletid = :walletId AND (uc.userclientid = w.userclientid OR up.userpromoterid = w.userpromoterid))
    WHERE w.walletid = :walletId
""")
    Flux<PaymentDetailsPromoterDTO> findPaymentDetailsByWalletId(Integer walletId);

    @Query("""
        SELECT 
            wt.wallettransactionid,
            wt.walletid,
            wt.currencytypeid,
            wt.transactioncategoryid,
            wt.inicialdate,
            wt.amount,
            wt.avalibledate,
            wt.description,
            wt.motivedescription,
            wt.operationcode,
            tcw.transactioncategoryname AS transactionCategoryName
        FROM wallettransaction wt
        JOIN transactioncategorywallet tcw ON wt.transactioncategoryid = tcw.transactioncategoryid
        WHERE wt.walletid = :walletId
        ORDER BY wt.inicialdate DESC
    """)
    Flux<com.proriberaapp.ribera.Api.controllers.client.dto.WalletTransactionDTO> findMovementsWithCategoryNameByWalletId(Integer walletId);

}
