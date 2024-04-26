package com.glaitozen.tableorganizer.core.mapper.h2;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface H2Mapper<Model, Dto> {

    Dto mapToDto(Model model);

    List<Dto> mapToDtoList(Collection<Model> model);

    Model mapToModel(Dto dto);

    List<Model> mapToModelList(Collection<Dto> dto);

    Set<Model> mapToModelSet(Collection<Dto> dto);
}
