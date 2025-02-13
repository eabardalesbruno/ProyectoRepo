package com.proriberaapp.ribera;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.Domain.dto.PointTypeDto;
import com.proriberaapp.ribera.services.point.PointConversionDto;
import com.proriberaapp.ribera.services.point.PointTransactionTypeEnum;
import com.proriberaapp.ribera.services.point.PointTransferRequestDto;
import com.proriberaapp.ribera.services.point.PointsTransactionService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class PointsTransactionServiceTest {
        @Autowired
        private PointsTransactionService pointsTransactionService;

        @Test
        void savePointConversion() {
                PointTypeDto pointTypeDto = PointTypeDto.builder()
                                .pointstypeid(2).pointstypedesc("Ribera")
                                .factor(0.5).build();
                PointConversionDto pointConversionDto = PointConversionDto.builder()
                                .membershipName("PRUEBA")
                                .pointDebited(100)
                                .pointType(pointTypeDto)
                                .type(PointTransactionTypeEnum.EXCHANGE)
                                .userId(83)
                                .build();
                StepVerifier.create(pointsTransactionService.convertPoints(pointConversionDto))
                                .expectNextMatches(pointConversion -> pointConversion.getPointAcredited() == 50)
                                .verifyComplete();
        }

        @Test
        void saveErrorTransfer() {

                PointTransferRequestDto pointConversionDto = PointTransferRequestDto.builder()
                                .sourceUserId(84)
                                .targetUserId(84)
                                .type(PointTransactionTypeEnum.TRANSFER)
                                .pointsAmount(100)
                                .build();
                pointConversionDto.setUserId(83);

                StepVerifier.create(pointsTransactionService.transferPoints(pointConversionDto))
                                .expectError(IllegalArgumentException.class);
        }

        @Test
        void savePointTransfer() {
                PointTransferRequestDto pointConversionDto = PointTransferRequestDto.builder()
                                .sourceUserId(83)
                                .targetUserId(84)
                                .type(PointTransactionTypeEnum.TRANSFER)
                                .pointsAmount(100)
                                .build();
                pointConversionDto.setUserId(83);

                StepVerifier.create(pointsTransactionService.transferPoints(pointConversionDto).then())
                                .verifyComplete();
        }
}