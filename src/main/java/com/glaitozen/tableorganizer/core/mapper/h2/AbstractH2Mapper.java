package com.glaitozen.tableorganizer.core.mapper.h2;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractH2Mapper<Model, Dto> implements H2Mapper<Model, Dto> {

    @Override
    public List<Dto> mapToDtoList(Collection<Model> modelCollection) {
        return modelCollection.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<Model> mapToModelList(Collection<Dto> dtoCollection) {
        return dtoCollection.stream()
                .map(this::mapToModel)
                .toList();
    }

    @Override
    public Set<Model> mapToModelSet(Collection<Dto> dtoCollection) {
        return new HashSet<>(mapToModelList(dtoCollection));
    }
}
