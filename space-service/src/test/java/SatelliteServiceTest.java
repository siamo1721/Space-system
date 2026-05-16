import org.example.Main;
import org.example.domain.dto.param.impl.ImagingSatelliteParam;
import org.example.domain.entity.ImagingSatellite;
import org.example.domain.entity.Satellite;
import org.example.domain.service.SatelliteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
class SatelliteServiceTest {

    @Autowired
    private SatelliteService satelliteService;

    @Test
    void shouldCreateImagingSatellite() {

        ImagingSatelliteParam param =
                new ImagingSatelliteParam("IMG-1", 0.9, 1080);

        Satellite satellite = satelliteService.createSatellite(param);

        assertInstanceOf(ImagingSatellite.class, satellite);
        assertEquals("IMG-1", satellite.getName());
    }
}