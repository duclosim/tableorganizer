package com.glaitozen.tableorganizer.core.mapper.h2;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
import com.glaitozen.tableorganizer.repository.dto.PropositionDeDateH2Dto;
import org.springframework.stereotype.Component;

@Component
public class PropositionDeDateH2Mapper extends AbstractH2Mapper<PropositionDeDate, PropositionDeDateH2Dto>
        implements H2Mapper<PropositionDeDate, PropositionDeDateH2Dto> {

    private final UserH2Mapper userMapper;

    public PropositionDeDateH2Mapper(UserH2Mapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PropositionDeDateH2Dto mapToDto(PropositionDeDate model) {
        if (model == null) {
            return null;
        }
        return new PropositionDeDateH2Dto(model.id(), model.date(), userMapper.mapToDtoList(model.users()));
    }

    @Override
    public PropositionDeDate mapToModel(PropositionDeDateH2Dto dto) {
        if (dto == null) {
            return null;
        }
        return new PropositionDeDate(dto.getId(), dto.getDate(), userMapper.mapToModelSet(dto.getUsers()));
    }
}
