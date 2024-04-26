package testutils.factory.model;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;

import java.time.LocalDate;
import java.util.List;

import static com.glaitozen.tableorganizer.utils.DateUtilsTest.THREE_DAYS;
import static testutils.RandomLocalDateFactory.randomFutureLocalDate;
import static testutils.factory.model.UserFactory.USER_LIST;

public class PropositionDeDateFactory {
    private PropositionDeDateFactory() {}

    public static final List<PropositionDeDate> THREE_PROPOSITIONS = THREE_DAYS.stream()
            .map(PropositionDeDateFactory::createPropositionDeDate)
            .toList();

    public static PropositionDeDate createPropositionDeDate() {
        return createPropositionDeDate(randomFutureLocalDate());
    }

    public static PropositionDeDate createPropositionDeDate(LocalDate date) {
        PropositionDeDate propositionDeDate = new PropositionDeDate(date);
        propositionDeDate.users().addAll(USER_LIST);
        return propositionDeDate;
    }
}
