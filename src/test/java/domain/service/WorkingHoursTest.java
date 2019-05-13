package domain.service;

import domain.service.WorkingDays;
import domain.service.WorkingHours;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Map;

@SpringBootTest(classes = {WorkingDays.class, WorkingHours.class})
@ExtendWith(SpringExtension.class)
public class WorkingHoursTest {

    @Autowired
    private WorkingHours workingHours;

    @Test
    void calcWorkingHours() {
        var duration = new AbstractMap.SimpleEntry<>(LocalTime.of(11, 30), LocalTime.of(21, 00).minusNanos(1L));
        Assertions.assertEquals(duration, workingHours.calcWorkingHours("11:30 am - 9 pm"));
    }
}