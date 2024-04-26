package com.glaitozen.tableorganizer.core.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.glaitozen.tableorganizer.utils.DateUtilsTest.TODAY;
import static com.glaitozen.tableorganizer.utils.DateUtilsTest.TOMORROW;
import static com.glaitozen.tableorganizer.utils.DateUtilsTest.YESTERDAY;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void isAvailable_with_date_not_in_dateOccupees_should_return_true() {
        // GIVEN
        User user = new User("Dai Enkai Entei");
        user.addDateOccupeeCollection(Arrays.asList(TOMORROW, TODAY.plusDays(2)));
        // WHEN
        boolean result = user.isAvailable(TODAY);
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void isAvailable_with_date_already_in_dateOccupees_should_return_false() {
        // GIVEN
        User user = new User("Black Hole");
        user.addDateOccupeeCollection(Arrays.asList(TODAY, TOMORROW, TODAY.plusDays(2)));
        // WHEN
        boolean result = user.isAvailable(TODAY);
        // THEN
        assertThat(result).isFalse();
    }

    @Test
    void clearDateOccupees_should_remove_all_past_and_current_dates() {
        // GIVEN
        User user = new User("Ura renge");
        user.addDateOccupeeCollection(Arrays.asList(TOMORROW, TODAY, YESTERDAY, TODAY.minusDays(2)));
        // WHEN
        user.clearDateOccupees();
        // THEN
        assertThat(user.datesOccupees()).hasSize(2);
        assertThat(user.datesOccupees()).contains(TODAY, TOMORROW);
    }
}
