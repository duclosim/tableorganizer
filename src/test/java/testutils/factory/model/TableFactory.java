package testutils.factory.model;

import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.glaitozen.tableorganizer.core.model.Rappel.DEUX_JOURS_AVANT;
import static com.glaitozen.tableorganizer.core.model.Rappel.TROIS_JOURS_AVANT;
import static com.glaitozen.tableorganizer.core.model.Rappel.UN_JOUR_AVANT;
import static testutils.RandomLocalDateFactory.randomFutureLocalDate;
import static testutils.RandomNumber.randomInt;
import static testutils.factory.model.UserFactory.createUser;
import static testutils.factory.model.UserFactory.createUserList;

public class TableFactory {

    public static final int MIN_JOUEURS = 2;
    public static final int MAX_JOUEURS = 8;
    public static final int MIN_PROPOSITIONS_DE_DATE = 2;
    public static final int MAX_PROPOSITIONS_DE_DATE = 10;

    private TableFactory() {}

    public static Table createTable() {
        return createTable("nom table", createUser());
    }

    public static Table createTable(String nomTable, User mdJ) {
        Table table = new Table(nomTable, "systeme", mdJ);
        createUserList(randomInt(MIN_JOUEURS, MAX_JOUEURS)).forEach(table::addJoueur);
        table.addPropositions(IntStream.range(MIN_PROPOSITIONS_DE_DATE, MAX_PROPOSITIONS_DE_DATE)
                .mapToObj(i -> randomFutureLocalDate())
                .collect(Collectors.toSet()));
        table.confirmerProchaineDate(randomFutureLocalDate());
        table.addRappel(UN_JOUR_AVANT);
        table.addRappel(DEUX_JOURS_AVANT);
        table.addRappel(TROIS_JOURS_AVANT);
        return table;
    }

    public static List<Table> createTableList(int nbTables) {
        return IntStream.range(0, nbTables)
                .mapToObj(k -> createTable("table nb " + k, createUser("mdj nb " + k)))
                .toList();
    }
}
