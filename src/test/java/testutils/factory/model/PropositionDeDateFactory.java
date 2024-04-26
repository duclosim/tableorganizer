package testutils.factory.model;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
import testutils.RandomLocalDateFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static testutils.factory.model.UserFactory.USER_LIST;
import static com.glaitozen.tableorganizer.utils.DateUtilsTest.THREE_DAYS;

public class PropositionDeDateFactory {
    private PropositionDeDateFactory() {}

    public static final List<PropositionDeDate> THREE_PROPOSITIONS = THREE_DAYS.stream()
            .map(PropositionDeDateFactory::createPropositionDeDate)
            .toList();

    public static PropositionDeDate createPropositionDeDate() {
        return createPropositionDeDate(RandomLocalDateFactory.randomFutureLocalDate());
    }

    public static PropositionDeDate createPropositionDeDate(LocalDate date) {
        PropositionDeDate propositionDeDate = new PropositionDeDate(date);
        propositionDeDate.users().addAll(USER_LIST);
        return propositionDeDate;
    }

    public static Set<PropositionDeDate> createPropositionDeDateList(int nbPropositions) {
        return IntStream.range(0, nbPropositions)
                .mapToObj(i -> createPropositionDeDate())
                .collect(Collectors.toSet());
    }
}
