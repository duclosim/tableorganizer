package com.glaitozen.tableorganizer.core.mapper.h2;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractH2Mapper<M, D> implements H2Mapper<M, D> {

    @Override
    public List<D> mapToDtoList(Collection<M> modelCollection) {
        return modelCollection.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<M> mapToModelList(Collection<D> dtoCollection) {
        return dtoCollection.stream()
                .map(this::mapToModel)
                .toList();
    }

    @Override
    public Set<M> mapToModelSet(Collection<D> dtoCollection) {
        return new HashSet<>(mapToModelList(dtoCollection));
    }
}
