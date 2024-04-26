package testutils.factory.dto.h2;

import com.glaitozen.tableorganizer.repository.dto.PropositionDeDateH2Dto;

import java.time.LocalDate;
import java.util.UUID;

import static testutils.RandomLocalDateFactory.randomFutureLocalDate;
import static testutils.factory.dto.h2.UserH2DtoFactory.USER_H2_DTO_LIST;

public class PropositionDeDateH2DtoFactory {
    private PropositionDeDateH2DtoFactory() {}

    public static PropositionDeDateH2Dto createPropositionDeDateH2Dto() {
        return createPropositionDeDateH2Dto(randomFutureLocalDate());
    }

    public static PropositionDeDateH2Dto createPropositionDeDateH2Dto(LocalDate date) {
        return new PropositionDeDateH2Dto(UUID.randomUUID().toString(), date, USER_H2_DTO_LIST);
    }
}
