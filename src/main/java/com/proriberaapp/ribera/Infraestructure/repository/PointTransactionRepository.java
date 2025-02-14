package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.dto.PointTransactionExchangeRow;
import com.proriberaapp.ribera.Domain.dto.PointTransferRowDto;
import com.proriberaapp.ribera.Domain.entities.PointTransactionEntity;

import reactor.core.publisher.Flux;

@Repository
public interface PointTransactionRepository extends ReactiveCrudRepository<PointTransactionEntity, Integer> {

    @Query("""
            select ptype.factor as pointtypefactor,ptype.pointstypedesc as pointtypedesc,ptype.pointstypeid as pointtypeid,pt."id",pt.transactiontypeid,to_char(pc.created_at, 'YYYY-MM-dd HH24:MI:SS') as created_at,ptt."name" as transactiontypename,pc.membershipname,pc.pointdebited,pc.pointacredited,ptt.color as transactiontypecolor from pointstransaction pt
              join pointconversion pc on pc.transactionid=pt."id"
              join pointtransactiontype ptt on ptt."id"=pt.transactiontypeid
              join pointstype ptype on ptype.pointstypeid=pc.pointtypeid
            where pt.userid=:userId
                         """)
    Flux<PointTransactionExchangeRow> getPointTransactionExchangeRow(Integer userId);

    @Query("""
            select ptype.factor as pointtypefactor,ptype.pointstypedesc as pointtypedesc,ptype.pointstypeid as pointtypeid,pt."id",pt.transactiontypeid,to_char(pc.created_at, 'YYYY-MM-dd HH24:MI:SS') as created_at,ptt."name" as transactiontypename,pc.membershipname,pc.pointdebited,pc.pointacredited,ptt.color as transactiontypecolor from pointstransaction pt
              join pointconversion pc on pc.transactionid=pt."id"
              join pointtransactiontype ptt on ptt."id"=pt.transactiontypeid
              join pointstype ptype on ptype.pointstypeid=pc.pointtypeid;
                         """)
    Flux<PointTransactionExchangeRow> getPointTransactionExchangeRow();

    @Query("""
            select pt."id",pt.transactiontypeid,ptt."name" as transactiontypename,ptr.pointsamount,pt.created_at,ur.userclientid as userreceiveid,ur.firstname as userreceivename,ur.lastname  as userreceivelastname,uc.firstname as usersendername ,uc.lastname as usersenderlastname,uc.userclientid as usersenderid
             from pointstransaction pt
            join pointtransactiontype ptt on ptt."id"=pt.transactiontypeid
            join pointstransfers ptr on ptr.transactionid=pt."id"
            join userclient ur on ur.userclientid=ptr.receiveruserid
            join userclient uc on uc.userclientid=ptr.senderuserid
            """)
    Flux<PointTransferRowDto> getPointTransfer();

    @Query("""
            select pt."id",pt.transactiontypeid,ptt."name" as transactiontypename,ptr.pointsamount,to_char(ptr.datetransfer, 'YYYY-MM-dd HH24:MI:SS') as created_at,ur.userclientid as userreceiveid,ur.firstname as userreceivename,ur.lastname  as userreceivelastname,uc.firstname as usersendername ,uc.lastname as usersenderlastname,uc.userclientid as usersenderid
             from pointstransaction pt
            join pointtransactiontype ptt on ptt."id"=pt.transactiontypeid
            join pointstransfers ptr on ptr.transactionid=pt."id"
            join userclient ur on ur.userclientid=ptr.receiveruserid
            join userclient uc on uc.userclientid=ptr.senderuserid
            where pt.userid=:userId
            """)
    Flux<PointTransferRowDto> getPointTransfer(Integer userId);

}