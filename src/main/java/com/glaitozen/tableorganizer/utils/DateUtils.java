package com.glaitozen.tableorganizer.utils;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class DateUtils {

    private DateUtils() {
    }

    public static LocalDate cleanPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now()) ? null : date;
    }

    public static Set<LocalDate> cleanPastDates(Set<LocalDate> dates) {
        final LocalDate clearingLimit = LocalDate.now();
        return dates.stream()
                .filter(d -> d.isAfter(clearingLimit))
                .collect(Collectors.toSet());
    }

    public static Set<PropositionDeDate> cleanPastPropositionDeDates(Set<PropositionDeDate> dates) {
        final LocalDate clearingLimit = LocalDate.now();
        return dates.stream()
                .filter(pdd -> pdd.date().isAfter(clearingLimit))
                .collect(Collectors.toSet());
    }
}
