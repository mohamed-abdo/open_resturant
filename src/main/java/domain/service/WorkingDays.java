package domain.service;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.IntStream;

@Component
public class WorkingDays {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingDays.class);

    public DayOfWeek parseDayString(@NonNull String dayOfWeek) {
        Objects.requireNonNull(dayOfWeek);
        switch (dayOfWeek.trim().toLowerCase()) {
            case "sat":
                return DayOfWeek.SATURDAY;
            case "sun":
                return DayOfWeek.SUNDAY;
            case "mon":
                return DayOfWeek.MONDAY;
            case "tue":
                return DayOfWeek.TUESDAY;
            case "wed":
                return DayOfWeek.WEDNESDAY;
            case "thu":
                return DayOfWeek.THURSDAY;
            case "fri":
                return DayOfWeek.FRIDAY;
            default:
                throw new IllegalArgumentException(String.format("invalid input: %s", dayOfWeek));
        }

    }

    public Set<DayOfWeek> calcWorkingDays(@NonNull DayOfWeek dayFrom, @NonNull DayOfWeek dayTo) throws IllegalArgumentException {
        //ex Mon-Fri
        int fromDay = dayFrom.getValue();
        LOGGER.info("adding day from idx {}", fromDay);
        int toDay = dayTo.getValue();
        LOGGER.info("adding day to idx {}", toDay);
        if (fromDay > toDay)
            throw new IllegalArgumentException("From day must be less than or equal to day");
        Set<DayOfWeek> initialSet = new HashSet<>();
        IntStream.rangeClosed(fromDay, toDay)
                .forEach(i ->
                        initialSet.add(DayOfWeek.of(i))
                );
        return initialSet;
    }

    public Pair<DayOfWeek, DayOfWeek> extractPairs(@NonNull String daysPair) {
        Objects.requireNonNull(daysPair);
        var daysSplit = daysPair.split("-");
        if (daysSplit.length != 2)
            throw new IllegalArgumentException("expected string contains '-' ");
        return Pair.of(parseDayString(daysSplit[0]), parseDayString(daysSplit[1]));
    }

    public Set<DayOfWeek> parseWorkingDaysSheet(@NonNull String workingSheetStr) {
        Objects.requireNonNull(workingSheetStr);
        // ex "Mon-Thu, Sun";
        return Arrays.stream(workingSheetStr.split(","))
                .map(i -> {
                    if (!i.contains("-"))
                        i = String.format("%s-%s", i, i);
                    return extractPairs(i);
                }).map(p -> calcWorkingDays(p.getKey(), p.getValue()))
                .reduce(new HashSet<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }


}
