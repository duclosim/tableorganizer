package com.glaitozen.tableorganizer.core.mapper.h2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaitozen.tableorganizer.core.model.User;
import com.glaitozen.tableorganizer.repository.dto.UserH2Dto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Component
public class UserH2Mapper extends AbstractH2Mapper<User, UserH2Dto> implements H2Mapper<User, UserH2Dto> {

    private final ObjectMapper objectMapper;

    public UserH2Mapper(@Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UserH2Dto mapToDto(User model) {
        if (model == null) {
            return null;
        }
        String datesOccupees;
        try {
            datesOccupees = objectMapper.writeValueAsString(model.datesOccupees());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new UserH2Dto(model.id(), model.nom(), datesOccupees);
    }

    @Override
    public User mapToModel(UserH2Dto dto) {
        if (dto == null) {
            return null;
        }
        List<LocalDate> userDates;
        try {
            userDates = objectMapper.readValue(dto.getDatesOccupees(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new User(dto.getId(), dto.getNom(), new HashSet<>(userDates));
    }
}
