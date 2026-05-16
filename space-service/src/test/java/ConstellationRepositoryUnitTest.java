import org.example.domain.entity.SatelliteConstellation;
import org.example.domain.repository.ConstellationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConstellationRepositoryUnitTest {
    private ConstellationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ConstellationRepository();
    }

    private static final String CONSTELLATION_1 = "Орбита-1";
    private static final String CONSTELLATION_2 = "Орбита-2";

    @Test
    @DisplayName("Сохранение группировки - успешно")
    void shouldSaveConstellation() {
        SatelliteConstellation satelliteConstellation =
                new SatelliteConstellation(CONSTELLATION_1);
        repository.save(satelliteConstellation);

        assertTrue(repository.findAll().containsKey(CONSTELLATION_1));
    }

    @Test
    @DisplayName("Поиск существующей группировки по имени")
    void shouldAddSatelliteToConstellation() {
        SatelliteConstellation satelliteConstellation =
                new SatelliteConstellation(CONSTELLATION_2);
        repository.save(satelliteConstellation);

        SatelliteConstellation found =
                repository.findByName(CONSTELLATION_2);

        assertNotNull(found);
        assertEquals(CONSTELLATION_2, found.getConstellationName());
    }

    @Test
    @DisplayName("Поиск несуществующей группировки")
    void shouldReturnNullIfConstellationNotFound() {
        SatelliteConstellation found = repository.findByName("Нет");

        assertNotNull(found);
    }

    @Test
    @DisplayName("Поиск всех группировок")
    void shouldReturnConstellations() {

        repository.save(new SatelliteConstellation(CONSTELLATION_1));
        repository.save(new SatelliteConstellation(CONSTELLATION_2));

        Map<String, SatelliteConstellation> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.containsKey(CONSTELLATION_1));
        assertTrue(all.containsKey(CONSTELLATION_2));
    }

    @Test
    @DisplayName("Поиск по пустому репозиторию")
    void shouldReturnEmptyMapIfRepositoryIsEmpty() {

        Map<String, SatelliteConstellation> all = repository.findAll();

        assertTrue(all.isEmpty());
    }
}
