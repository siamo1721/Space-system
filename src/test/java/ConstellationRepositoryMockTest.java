import org.example.SatelliteConstellation;
import org.example.repository.ConstellationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConstellationRepositoryMockTest {

    @Mock
    private ConstellationRepository repository;

    private static final String CONSTELLATION_NAME = "Орбита-Мок";
    private static final String UNKNOWN_NAME = "Неизвестная-Орбита";

    @Test
    @DisplayName("Получение всех группировок через mock")
    void shouldReturnAllConstellations() {

        SatelliteConstellation constellation =
                new SatelliteConstellation(CONSTELLATION_NAME);

        Map<String, SatelliteConstellation> constellations = new HashMap<>();
        constellations.put(CONSTELLATION_NAME, constellation);

        when(repository.findAll()).thenReturn(constellations);

        Map<String, SatelliteConstellation> result = repository.findAll();

        assertTrue(result.containsKey(CONSTELLATION_NAME));

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Поиск группировки по имени — успешно")
    void shouldFindConstellationByName() {

        SatelliteConstellation constellation =
                new SatelliteConstellation(CONSTELLATION_NAME);

        when(repository.findByName(CONSTELLATION_NAME))
                .thenReturn(constellation);

        SatelliteConstellation result =
                repository.findByName(CONSTELLATION_NAME);

        assertEquals(CONSTELLATION_NAME, result.getConstellationName());

        verify(repository).findByName(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("Поиск несуществующей группировки")
    void shouldReturnNullWhenConstellationNotFound() {

        when(repository.findByName(UNKNOWN_NAME))
                .thenReturn(null);

        SatelliteConstellation result =
                repository.findByName(UNKNOWN_NAME);

        assertNull(result);

        verify(repository).findByName(UNKNOWN_NAME);
    }

    @Test
    @DisplayName("Получение всех группировок — репозиторий пуст")
    void shouldReturnEmptyMapWhenNoConstellationsExist() {

        when(repository.findAll())
                .thenReturn(new HashMap<>());

        Map<String, SatelliteConstellation> result =
                repository.findAll();

        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    @DisplayName("Сохранение null-группировки")
    void shouldHandleNullConstellationSave() {

        doNothing().when(repository).save(null);

        repository.save(null);

        verify(repository).save(null);
    }

    @Test
    @DisplayName("Повторное сохранение одной и той же группировки")
    void shouldAllowSavingSameConstellationTwice() {

        SatelliteConstellation constellation =
                new SatelliteConstellation(CONSTELLATION_NAME);

        doNothing().when(repository).save(constellation);

        repository.save(constellation);
        repository.save(constellation);

        verify(repository, times(2)).save(constellation);
    }
}