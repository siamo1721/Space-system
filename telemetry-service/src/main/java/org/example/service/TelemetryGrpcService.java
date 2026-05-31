package org.example.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.kafka.SatelliteRegistry;
import org.example.telemetry.TelemetryServiceGrpc;
import org.example.telemetry.TelemetryRequest;
import org.example.telemetry.TelemetryUpdate;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class TelemetryGrpcService extends TelemetryServiceGrpc.TelemetryServiceImplBase {

    private final SatelliteRegistry satelliteRegistry;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
    private final Random random = new Random();

    @Override
    public void streamTelemetry(TelemetryRequest request, StreamObserver<TelemetryUpdate> responseObserver) {
        log.info("Начало трансляции телеметрии для клиента: {}", request.getSatelliteId());

        executorService.scheduleAtFixedRate(() -> {
            try {
                Set<String> satelliteNames = satelliteRegistry.getSatelliteNames();
                if (satelliteNames.isEmpty()) {
                    log.debug("Реестр спутников пуст — телеметрия не отправляется");
                    return;
                }

                for (String satelliteName : satelliteNames) {
                    double insideTemp = 20 + random.nextDouble() * 40;
                    double outsideTemp = -100 + random.nextDouble() * 200;

                    TelemetryUpdate update = TelemetryUpdate.newBuilder()
                            .setSatelliteId(satelliteName)
                            .setInsideTemperature(insideTemp)
                            .setOutsideTemperature(outsideTemp)
                            .build();

                    responseObserver.onNext(update);
                    log.debug("Отправлена телеметрия: {} - внутри: {}°C, снаружи: {}°C",
                            satelliteName, insideTemp, outsideTemp);
                }
            } catch (Exception e) {
                log.error("Ошибка при отправке телеметрии", e);
                responseObserver.onError(e);
            }
        }, 0, 2, TimeUnit.SECONDS);
    }
}
