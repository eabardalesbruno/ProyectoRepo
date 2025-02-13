package com.proriberaapp.ribera;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.proriberaapp.ribera.Domain.entities.PointTransactionEntity;
import com.proriberaapp.ribera.Domain.entities.PointsTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointTransactionRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTypeRepository;
import com.proriberaapp.ribera.services.point.PointTransactionRequestDto;
import com.proriberaapp.ribera.services.point.PointTransactionTypeEnum;
import com.proriberaapp.ribera.services.point.PointTransferRequestDto;
import com.proriberaapp.ribera.services.point.PointsTransactionService;
import com.proriberaapp.ribera.services.point.PointsTransactionStrategy;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class PointsTransactionServiceTest {

        @Mock
        private PointTransactionRepository pointTransactionRepository;

        @Mock
        private PointsTypeRepository pointsTypeRepository;

        @Mock
        private PointsTransactionStrategy<PointTransactionRequestDto> mockStrategy;

        private PointsTransactionService service;
        private Map<PointTransactionTypeEnum, PointsTransactionStrategy<?>> strategies;

        @BeforeEach
        void setUp() {
                strategies = new HashMap<>();
                strategies.put(PointTransactionTypeEnum.TRANSFER, mockStrategy);
                service = new PointsTransactionService(strategies, pointTransactionRepository, pointsTypeRepository);
        }

        @Test
        void processTransaction_ValidRequest_Success() {
                PointTransactionRequestDto request = new PointTransactionRequestDto();
                request.setType(PointTransactionTypeEnum.TRANSFER);
                request.setUserId(1);

                PointsTypeEntity pointType = new PointsTypeEntity();
                pointType.setPointstypeid(1);

                PointTransactionEntity savedTransaction = PointTransactionEntity.builder()
                                .id(1).build();

                when(pointsTypeRepository.findByName(request.getType().getDescription()))
                                .thenReturn(Mono.just(pointType));
                when(pointTransactionRepository.save(any()))
                                .thenReturn(Mono.just(savedTransaction));
                when(mockStrategy.execute(any()))
                                .thenReturn(Mono.just(request));

                StepVerifier.create(service.processTransaction(request))
                                .expectNext(request)
                                .verifyComplete();
        }

        @Test
        void processTransaction_InvalidType_Error() {
                PointTransactionRequestDto request = new PointTransactionRequestDto();
                request.setType(null);

                StepVerifier.create(service.processTransaction(request))
                                .expectError(IllegalArgumentException.class)
                                .verify();
        }

        @Test
        void transferPoints_ValidRequest_Success() {
                PointTransferRequestDto request = new PointTransferRequestDto();
                request.setUserId(1);

                PointsTypeEntity pointType = new PointsTypeEntity();
                pointType.setPointstypeid(1);

                PointTransactionEntity savedTransaction = PointTransactionEntity.builder()
                                .id(1)
                                .build();

                when(pointsTypeRepository.findByName(PointTransactionTypeEnum.TRANSFER.getDescription()))
                                .thenReturn(Mono.just(pointType));
                when(pointTransactionRepository.save(any()))
                                .thenReturn(Mono.just(savedTransaction));
                when(mockStrategy.execute(any()))
                                .thenReturn(Mono.just(request));

                StepVerifier.create(service.transferPoints(request))
                                .expectNext(request)
                                .verifyComplete();
        }
}