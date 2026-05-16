import org.example.Main;
import org.example.domain.repository.ConstellationRepository;
import org.example.domain.service.ConstellationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
class ConstellationRepositoryIntegrationTest {

    @Autowired
    private ConstellationRepository repository;

    @Autowired
    private ConstellationService service;

    private static final String CONSTELLATION_NAME = "Орбита-Интеграция";

    @Test
    @DisplayName("Полный жизненный цикл спутниковой группировки")
    void fullLifecycleTest() {

        service.createAndSaveConstellation(CONSTELLATION_NAME);

        Satellite satellite = new Satellite("Связь-INT", 1.0) {
            @Override
            protected void performMission() {}
        };

        service.addSatelliteToConstellation(CONSTELLATION_NAME, satellite);

        assertEquals(
                1,
                repository.findByName(CONSTELLATION_NAME)
                        .getSatellite()
                        .size()
        );

        service.activateAllSatellites(CONSTELLATION_NAME);

        assertTrue(satellite.getState().isActive());

        service.executeConstellationMission(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("Добавление спутника в несуществующую группировку")
    void shouldFailWhenConstellationDoesNotExist() {

        Satellite satellite = new Satellite("Связь-404", 0.5) {
            @Override
            protected void performMission() {}
        };

        assertThrows(
                RuntimeException.class,
                () -> service.addSatelliteToConstellation("НЕ-СУЩЕСТВУЕТ", satellite)
        );
    }

    @Test
    @DisplayName("Активация спутников в пустой группировке")
    void shouldHandleActivationWithNoSatellites() {

        service.createAndSaveConstellation("Пустая-Орбита");

        assertDoesNotThrow(
                () -> service.activateAllSatellites("Пустая-Орбита")
        );

        assertEquals(
                0,
                repository.findByName("Пустая-Орбита")
                        .getSatellite()
                        .size()
        );
    }

    @Test
    @DisplayName("Создание группировки с пустым именем")
    void shouldCreateConstellationWithEmptyName() {

        service.createAndSaveConstellation("");

        assertNotNull(repository.findByName(""));
    }

    @Test
    @DisplayName("Повторное создание группировки с одинаковым именем")
    void shouldOverrideConstellationWithSameName() {

        service.createAndSaveConstellation(CONSTELLATION_NAME);
        service.createAndSaveConstellation(CONSTELLATION_NAME);

        assertNotNull(repository.findByName(CONSTELLATION_NAME));
    }
}