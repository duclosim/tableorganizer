package com.glaitozen.tableorganizer.core.mapper.h2;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface H2Mapper<M, D> {

    D mapToDto(M m);

    List<D> mapToDtoList(Collection<M> m);

    M mapToModel(D d);

    List<M> mapToModelList(Collection<D> d);

    Set<M> mapToModelSet(Collection<D> d);
}
