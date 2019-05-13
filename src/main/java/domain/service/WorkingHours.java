package domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

@Component
public class WorkingHours {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingHours.class);
    private final String timeFormat = "h[:m] a";//with optional minutes

    public Map.Entry<LocalTime, LocalTime> calcWorkingHours(@NonNull String timeInStr) {
        Objects.requireNonNull(timeInStr);
        String[] timeSplit = timeInStr.toUpperCase().split("-");//in case am | pm will be in CAPS
        if (timeSplit.length == 1)
            throw new IllegalArgumentException(String.format("invalid working hours format. %s", timeInStr));
        String fromTimeStr = timeSplit[0].trim();
        String toTimeInStr = timeSplit[1].trim();

        String[] fromTimeSplit = fromTimeStr.split(" ");
        if (fromTimeSplit.length == 1)
            throw new IllegalArgumentException(String.format("invalid working hours format. %s", timeInStr));

        return new AbstractMap.SimpleEntry<>(LocalTime.parse(fromTimeStr, DateTimeFormatter.ofPattern(timeFormat)),
                LocalTime.parse(toTimeInStr, DateTimeFormatter.ofPattern(timeFormat)).minusNanos(1L));
    }

}