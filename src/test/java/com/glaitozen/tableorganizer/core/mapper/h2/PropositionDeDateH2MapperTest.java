package com.glaitozen.tableorganizer.core.mapper.h2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
import com.glaitozen.tableorganizer.repository.dto.PropositionDeDateH2Dto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.glaitozen.tableorganizer.core.mapper.h2.UserH2MapperTest.assertMapUserListToModelSetIsOk;
import static com.glaitozen.tableorganizer.core.mapper.h2.UserH2MapperTest.assertMapUserSetToDtoListIsOk;
import static org.assertj.core.api.Assertions.assertThat;
import static testutils.factory.dto.h2.PropositionDeDateH2DtoFactory.createPropositionDeDateH2Dto;
import static testutils.factory.model.PropositionDeDateFactory.createPropositionDeDate;

class PropositionDeDateH2MapperTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final UserH2Mapper userMapper = new UserH2Mapper(objectMapper);
    private static final PropositionDeDateH2Mapper mapper = new PropositionDeDateH2Mapper(userMapper);

    @BeforeAll
    static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test
    void mapToDto_with_a_null_parameter_should_return_null() {
        // GIVEN
        PropositionDeDate model = null;
        // WHEN
        PropositionDeDateH2Dto result = mapper.mapToDto(model);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    void mapToDto_should_map_all_the_fields_correctly() {
        // GIVEN
        PropositionDeDate model = createPropositionDeDate();
        // WHEN
        PropositionDeDateH2Dto result = mapper.mapToDto(model);
        // THEN
        assertMapToDtoIsOk(result, model, objectMapper);
    }

    private static void assertMapToDtoIsOk(PropositionDeDateH2Dto result, PropositionDeDate model, ObjectMapper objectMapper) {
        assertThat(result.getId()).isEqualTo(model.id());
        assertThat(result.getDate()).isEqualTo(model.date());
        assertMapUserSetToDtoListIsOk(result.getUsers(), model.users(), objectMapper);
    }

    static void assertMapModelSetToDtoListIsOk(List<PropositionDeDateH2Dto> resultList, Set<PropositionDeDate> modelSet,
                                               ObjectMapper objectMapper) {
        assertThat(resultList).hasSameSizeAs(modelSet);
        resultList.forEach(propositionDto -> {
            Optional<PropositionDeDate> matchingProposition = modelSet.stream()
                    .filter(ppd -> Objects.equals(ppd.id(), propositionDto.getId()))
                    .findAny();
            assertThat(matchingProposition).isPresent();
            assertMapToDtoIsOk(propositionDto, matchingProposition.get(), objectMapper);
        });
    }

    @Test
    void mapToModel_with_a_null_parameter_should_return_null() {
        // GIVEN
        PropositionDeDateH2Dto dto = null;
        // WHEN
        PropositionDeDate result = mapper.mapToModel(dto);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    void mapToModel_should_map_all_the_fields_correctly() {
        // GIVEN
        PropositionDeDateH2Dto dto = createPropositionDeDateH2Dto();
        // WHEN
        PropositionDeDate result = mapper.mapToModel(dto);
        // THEN
        assertMapToModelIsOk(result, dto, objectMapper);
    }

    private static void assertMapToModelIsOk(PropositionDeDate result, PropositionDeDateH2Dto dto, ObjectMapper objectMapper) {
        assertThat(result.id()).isEqualTo(dto.getId());
        assertThat(result.date()).isEqualTo(dto.getDate());
        assertMapUserListToModelSetIsOk(result.users(), dto.getUsers(), objectMapper);
    }

    static void assertMapDtoListToModelSetIsOk(Set<PropositionDeDate> resultSet, List<PropositionDeDateH2Dto> dtoList,
                                               ObjectMapper objectMapper) {
        assertThat(resultSet).hasSameSizeAs(dtoList);
        resultSet.forEach(proposition -> {
            Optional<PropositionDeDateH2Dto> matchingProposition = dtoList.stream()
                    .filter(pdd -> Objects.equals(pdd.getId(), proposition.id()))
                    .findAny();
            assertThat(matchingProposition).isPresent();
            assertMapToModelIsOk(proposition, matchingProposition.get(), objectMapper);
        });
    }
}
