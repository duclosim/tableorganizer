package testutils.factory.dto.h2;

import com.glaitozen.tableorganizer.repository.dto.PropositionDeDateH2Dto;
import com.glaitozen.tableorganizer.repository.dto.TableH2Dto;
import com.glaitozen.tableorganizer.repository.dto.UserH2Dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static testutils.RandomLocalDateFactory.randomFutureLocalDate;
import static testutils.RandomNumber.randomInt;
import static testutils.factory.dto.h2.UserH2DtoFactory.createUserH2DTOList;
import static testutils.factory.dto.h2.UserH2DtoFactory.createUserH2Dto;
import static testutils.factory.model.TableFactory.MAX_JOUEURS;
import static testutils.factory.model.TableFactory.MAX_PROPOSITIONS_DE_DATE;
import static testutils.factory.model.TableFactory.MIN_JOUEURS;
import static testutils.factory.model.TableFactory.MIN_PROPOSITIONS_DE_DATE;

public class TableH2DtoFactory {
    private TableH2DtoFactory() {}

    public static TableH2Dto createTableH2Dto() {
        return createTableH2Dto("nom table", createUserH2Dto("mdj default"));
    }

    public static TableH2Dto createTableH2Dto(String nomTable, UserH2Dto mdJ) {
        List<UserH2Dto> joueurs = createUserH2DTOList(randomInt(MIN_JOUEURS, MAX_JOUEURS));
        TableH2Dto dto = new TableH2Dto(UUID.randomUUID().toString(), nomTable, "D&d", mdJ, joueurs,
                IntStream.range(MIN_PROPOSITIONS_DE_DATE, randomInt(0, MAX_PROPOSITIONS_DE_DATE))
                        .mapToObj(i -> new PropositionDeDateH2Dto(UUID.randomUUID().toString(), randomFutureLocalDate(), joueurs))
                        .toList(),
                randomFutureLocalDate(), true, false, false);
        dto.getPropositions().forEach(ppd -> {
            List<UserH2Dto> ppdUsers = new ArrayList<>(ppd.getUsers());
            ppdUsers.add(mdJ);
            ppd.setUsers(ppdUsers);
        });
        return dto;
    }

    public static List<TableH2Dto> createTableH2DTOList(int nbDto) {
        return IntStream.range(0, nbDto)
                .mapToObj(k -> createTableH2Dto("table nb " + k, createUserH2Dto("mdJ nb " + k)))
                .toList();
    }
}
