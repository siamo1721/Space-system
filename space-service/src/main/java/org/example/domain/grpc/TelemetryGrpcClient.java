package org.example.domain.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.entity.Satellite;
import org.example.domain.repository.SatelliteRepository;
import org.example.telemetry.TelemetryRequest;
import org.example.telemetry.TelemetryServiceGrpc;
import org.example.telemetry.TelemetryUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class TelemetryGrpcClient {

    @Value("${grpc.telemetry.host:localhost}")
    private String telemetryHost;

    @Value("${grpc.telemetry.port:9091}")
    private int telemetryPort;

    @Autowired
    private SatelliteRepository satelliteRepository;

    private TelemetryServiceGrpc.TelemetryServiceStub telemetryStub;

    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(telemetryHost, telemetryPort)
                .usePlaintext()
                .build();

        telemetryStub = TelemetryServiceGrpc.newStub(channel);
        log.info("gRPC клиент для telemetry инициализирован на {}:{}", telemetryHost, telemetryPort);

        startListeningToTelemetry();
    }

    private void startListeningToTelemetry() {
        TelemetryRequest request = TelemetryRequest.newBuilder()
                .setSatelliteId("space-server-client")
                .build();

        final CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<TelemetryUpdate> responseObserver = new StreamObserver<TelemetryUpdate>() {
            @Override
            public void onNext(TelemetryUpdate update) {
                log.debug("Получена телеметрия: {} - внутри: {:.1f}°C, снаружи: {:.1f}°C",
                        update.getSatelliteId(), update.getInternalTemperature(), update.getExternalTemperature());

                // Обновляем данные спутника в БД
                try {
                    satelliteRepository.findByName(update.getSatelliteId())
                            .ifPresent(satellite -> {
                                satellite.setInternalTemperature(update.getInternalTemperature());
                                satellite.setExternalTemperature(update.getExternalTemperature());
                                satelliteRepository.save(satellite);
                                log.debug("Спутник {} обновлен в БД", update.getSatelliteId());
                            });
                } catch (Exception e) {
                    log.error("Ошибка при обновлении спутника в БД", e);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Ошибка в потоке телеметрии", t);
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("Поток телеметрии завершен");
                latch.countDown();
            }
        };

        telemetryStub.streamTelemetry(request, responseObserver);
    }
}

