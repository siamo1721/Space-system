package org.example.service;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.telemetry.TelemetryServiceGrpc;
import org.example.telemetry.TelemetryRequest;
import org.example.telemetry.TelemetryUpdate;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@GrpcService
public class TelemetryGrpcService extends TelemetryServiceGrpc.TelemetryServiceImplBase {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
    private final Random random = new Random();

    private static final String[] SATELLITE_NAMES = {
        "Связь-1",
        "IMG-1",
        "Связь-2"
    };

    @Override
    public void streamTelemetry(TelemetryRequest request, StreamObserver<TelemetryUpdate> responseObserver) {
        log.info("Начало трансляции телеметрии для клиента: {}", request.getSatelliteId());

        executorService.scheduleAtFixedRate(() -> {
            try {
                for (String satelliteName : SATELLITE_NAMES) {

                    double insideTemp = 20 + random.nextDouble() * 40;
                    double outsideTemp = -100 + random.nextDouble() * 200;

                    TelemetryUpdate update = TelemetryUpdate.newBuilder()
                            .setSatelliteId(satelliteName)
                            .setInsideTemperature(insideTemp)
                            .setOutsideTemperature(outsideTemp)
                            .build();

                    responseObserver.onNext(update);
                    log.debug("Отправлена телеметрия: {} - внутри: {:.1f}°C, снаружи: {:.1f}°C",
                            satelliteName, insideTemp, outsideTemp);
                }
            } catch (Exception e) {
                log.error("Ошибка при отправке телеметрии", e);
                responseObserver.onError(e);
            }
        }, 0, 2, TimeUnit.SECONDS);
    }
}
