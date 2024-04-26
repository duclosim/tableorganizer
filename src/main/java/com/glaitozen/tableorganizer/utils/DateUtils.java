package com.glaitozen.tableorganizer.utils;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DateUtils {

    private DateUtils() {
    }

    public static LocalDate cleanPastDate(LocalDate date) {
        final LocalDate clearingLimit = LocalDate.now();
        return date != null && floorLocalDatePredicate(clearingLimit).test(date) ? date : null;
    }

    public static Set<LocalDate> cleanPastDateCollection(Collection<LocalDate> dates) {
        final LocalDate clearingLimit = LocalDate.now();
        return dates.stream()
                .filter(floorLocalDatePredicate(clearingLimit))
                .collect(Collectors.toSet());
    }

    public static Set<PropositionDeDate> cleanPastPropositionDeDateCollection(Collection<PropositionDeDate> dates) {
        final LocalDate clearingLimit = LocalDate.now();
        return dates.stream()
                .filter(pdd -> floorLocalDatePredicate(clearingLimit).test(pdd.date()))
                .collect(Collectors.toSet());
    }

    private static Predicate<LocalDate> floorLocalDatePredicate(LocalDate clearingLimit) {
        return localDate -> !localDate.isBefore(clearingLimit);
    }
}
