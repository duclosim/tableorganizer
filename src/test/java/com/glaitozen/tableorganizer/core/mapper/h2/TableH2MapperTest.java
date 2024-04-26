package com.glaitozen.tableorganizer.core.mapper.h2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
import com.glaitozen.tableorganizer.core.model.Rappel;
import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.model.User;
import com.glaitozen.tableorganizer.repository.dto.TableH2Dto;
import com.glaitozen.tableorganizer.repository.dto.UserH2Dto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.glaitozen.tableorganizer.core.mapper.h2.UserH2MapperTest.assertMapUserToDtoIsOk;
import static com.glaitozen.tableorganizer.core.mapper.h2.UserH2MapperTest.assertMapUserToModelIsOk;
import static com.glaitozen.tableorganizer.core.model.Rappel.DEUX_JOURS_AVANT;
import static com.glaitozen.tableorganizer.core.model.Rappel.TROIS_JOURS_AVANT;
import static com.glaitozen.tableorganizer.core.model.Rappel.UN_JOUR_AVANT;
import static org.assertj.core.api.Assertions.assertThat;
import static testutils.factory.dto.h2.TableH2DtoFactory.createTableH2Dto;
import static testutils.factory.model.TableFactory.createTable;

class TableH2MapperTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final UserH2Mapper userMapper = new UserH2Mapper(objectMapper);
    private static final TableH2Mapper mapper = new TableH2Mapper(new PropositionDeDateH2Mapper(userMapper), userMapper);


    @BeforeAll
    static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test
    void mapToDto_with_a_null_parameter_should_return_null() {
        // GIVEN
        Table model = null;
        // WHEN
        TableH2Dto result = mapper.mapToDto(model);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    void mapToDto_should_map_all_the_fields_correctly() {
        // GIVEN
        Table model = createTable();
        // WHEN
        TableH2Dto result = mapper.mapToDto(model);
        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(model.getId());
        assertThat(result.getNom()).isEqualTo(model.getNom());
        assertThat(result.getSysteme()).isEqualTo(model.getSysteme());
        assertMapUserToDtoIsOk(result.getMdJ(), model.getMdJ(), objectMapper);
        assertThat(result.getJoueurs()).hasSameSizeAs(model.getJoueurs());
        result.getJoueurs().forEach(joueurDto -> {
            Optional<User> matchingUserModel = model.getJoueurs().stream()
                    .filter(joueur -> Objects.equals(joueur.id(), joueurDto.getId()))
                    .findAny();
            assertThat(matchingUserModel).isPresent();
            assertMapUserToDtoIsOk(joueurDto, matchingUserModel.get(), objectMapper);
        });
        assertThat(result.getPropositions()).hasSameSizeAs(model.getPropositions());
        result.getPropositions().forEach(propositionDto -> {
            Optional<PropositionDeDate> matchingProposition = model.getPropositions().stream()
                    .filter(ppd -> Objects.equals(ppd.id(), propositionDto.getId()))
                    .findAny();
            assertThat(matchingProposition).isPresent();
            // TODO proposition de date asserts
        });

        assertThat(result.getProchaineDate()).isEqualTo(model.getProchaineDate());
        if (result.isRappelUn()) {
            assertThat(model.getRappels()).contains(UN_JOUR_AVANT);
        } else {
            assertThat(model.getRappels()).doesNotContain(UN_JOUR_AVANT);
        }
        if (result.isRappelDeux()) {
            assertThat(model.getRappels()).contains(DEUX_JOURS_AVANT);
        } else {
            assertThat(model.getRappels()).doesNotContain(DEUX_JOURS_AVANT);
        }
        if (result.isRappelTrois()) {
            assertThat(model.getRappels()).contains(TROIS_JOURS_AVANT);
        } else {
            assertThat(model.getRappels()).doesNotContain(TROIS_JOURS_AVANT);
        }
    }

    @Test
    void mapToModel_with_a_null_parameter_should_return_null() {
        // GIVEN
        Table model = null;
        // WHEN
        TableH2Dto result = mapper.mapToDto(model);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    void mapToModel_should_map_all_the_fields_correctly() {
        // GIVEN
        TableH2Dto dto = createTableH2Dto();
        // WHEN
        Table result = mapper.mapToModel(dto);
        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getNom()).isEqualTo(dto.getNom());
        assertThat(result.getSysteme()).isEqualTo(dto.getSysteme());
        assertMapUserToModelIsOk(result.getMdJ(), dto.getMdJ(), objectMapper);
        assertThat(result.getJoueurs()).hasSameSizeAs(dto.getJoueurs());
        result.getJoueurs().forEach(joueur -> {
            Optional<UserH2Dto> matchingJoueur = dto.getJoueurs().stream()
                    .filter(joueurDto -> Objects.equals(joueurDto.getId(), joueur.id()))
                    .findAny();
            assertThat(matchingJoueur).isPresent();
            assertMapUserToModelIsOk(joueur, matchingJoueur.get(), objectMapper);
        });
        // TODO assert joueurs
        assertThat(result.getPropositions()).hasSameSizeAs(dto.getPropositions());
        // TODO assert pp de date
        assertThat(result.getProchaineDate()).isEqualTo(dto.getProchaineDate());
        long nbRappels = 0L;
        for (Rappel rappel : result.getRappels()) {
            switch (rappel) {
                case UN_JOUR_AVANT -> {
                    assertThat(dto.isRappelUn()).isTrue();
                    ++nbRappels;
                }
                case DEUX_JOURS_AVANT -> {
                    assertThat(dto.isRappelDeux()).isTrue();
                    ++nbRappels;
                }
                case TROIS_JOURS_AVANT -> {
                    assertThat(dto.isRappelTrois()).isTrue();
                    ++nbRappels;
                }
            }
        }
        long nbFlags = Stream.of(dto.isRappelUn(), dto.isRappelDeux(), dto.isRappelTrois()).filter(b -> b).count();
        assertThat(nbRappels).isEqualTo(nbFlags);
    }

    @Test
    void mapToRappel_should_add_every_enum_when_flags_are_true() {
        // GIVEN
        boolean rappelUn = true;
        boolean rappelDeux = false;
        boolean rappelTrois = true;
        // WHEN
        Set<Rappel> rappelSet = mapper.mapToRappel(rappelUn, rappelDeux, rappelTrois);
        // THEN
        assertThat(rappelSet).hasSize(2);
        assertThat(rappelSet).contains(UN_JOUR_AVANT, TROIS_JOURS_AVANT);
    }
}
