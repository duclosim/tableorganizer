package com.glaitozen.tableorganizer.utils;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static testutils.factory.model.PropositionDeDateFactory.THREE_PROPOSITIONS;

public class DateUtilsTest {

    public static final LocalDate TODAY = LocalDate.now();
    public static final LocalDate YESTERDAY = TODAY.minusDays(1);
    public static final LocalDate TOMORROW = TODAY.plusDays(1);
    public static final List<LocalDate> THREE_DAYS = Arrays.asList(YESTERDAY, TODAY, TOMORROW);

    @Test
    void cleanPastDate_with_yesterday_date_should_return_null() {
        // WHEN
        LocalDate result = DateUtils.cleanPastDate(YESTERDAY);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    void cleanPastDate_with_today_date_should_return_today() {
        // WHEN
        LocalDate result = DateUtils.cleanPastDate(TODAY);
        // THEN
        assertThat(result).isNotNull();
    }

    @Test
    void cleanPastDate_with_tomorrow_date_should_return_tomorrow() {
        // WHEN
        LocalDate result = DateUtils.cleanPastDate(TOMORROW);
        // THEN
        assertThat(result).isNotNull();
    }

    @Test
    void cleanPastDateCollection_should_retain_only_today_and_dates_after_today() {
        // WHEN
        Set<LocalDate> result = DateUtils.cleanPastDateCollection(THREE_DAYS);
        // THEN
        assertThat(result).hasSize(2);
        assertThat(result).contains(TODAY, TOMORROW);
    }

    @Test
    void cleanPastPropositionDeDates_should_retain_only_today_and_dateCollection_after_today() {
        // WHEN
        Set<PropositionDeDate> result = DateUtils.cleanPastPropositionDeDateCollection(THREE_PROPOSITIONS);
        // THEN
        assertThat(result).hasSize(2);
        THREE_PROPOSITIONS.forEach(ppd -> {
            if (!ppd.date().isBefore(TODAY)) {
                assertThat(result).contains(ppd);
            }
        });
    }
}
