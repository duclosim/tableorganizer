package com.glaitozen.tableorganizer.core.mapper.h2;

import com.glaitozen.tableorganizer.core.model.Rappel;
import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.repository.dto.TableH2Dto;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class TableH2Mapper extends AbstractH2Mapper<Table, TableH2Dto> implements H2Mapper<Table, TableH2Dto> {

    private final PropositionDeDateH2Mapper propositionDeDateH2Mapper;
    private final UserH2Mapper userH2Mapper;

    public TableH2Mapper(PropositionDeDateH2Mapper propositionDeDateH2Mapper, UserH2Mapper userH2Mapper) {
        this.propositionDeDateH2Mapper = propositionDeDateH2Mapper;
        this.userH2Mapper = userH2Mapper;
    }

    @Override
    public TableH2Dto mapToDto(Table model) {
        if (model == null) {
            return null;
        }
        boolean rappelUn, rappelDeux, rappelTrois;
        rappelUn = model.getRappels().contains(Rappel.UN_JOUR_AVANT);
        rappelDeux = model.getRappels().contains(Rappel.UN_JOUR_AVANT);
        rappelTrois = model.getRappels().contains(Rappel.UN_JOUR_AVANT);

        return new TableH2Dto(model.getId(), model.getNom(), model.getSysteme(), userH2Mapper.mapToDto(model.getMdJ()),
                userH2Mapper.mapToDtoList(model.getJoueurs()), propositionDeDateH2Mapper.mapToDtoList(model.getPropositions()),
                model.getProchaineDate(), rappelUn, rappelDeux, rappelTrois);
    }

    @Override
    public Table mapToModel(TableH2Dto dto) {
        if (dto == null) {
            return null;
        }
        return new Table(dto.getId(), dto.getNom(), dto.getSysteme(), userH2Mapper.mapToModel(dto.getMdJ()),
                userH2Mapper.mapToModelSet(dto.getJoueurs()), propositionDeDateH2Mapper.mapToModelSet(dto.getPropositions()),
                dto.getProchaineDate(), mapToRappel(dto.isRappelUn(), dto.isRappelDeux(), dto.isRappelTrois()));
    }

    Set<Rappel> mapToRappel(boolean rappelUn, boolean rappelDeux, boolean rappelTrois) {
        Set<Rappel> rappels = EnumSet.noneOf(Rappel.class);
        if (rappelUn) {
            rappels.add(Rappel.UN_JOUR_AVANT);
        }
        if (rappelDeux) {
            rappels.add(Rappel.DEUX_JOURS_AVANT);
        }
        if (rappelTrois) {
            rappels.add(Rappel.TROIS_JOURS_AVANT);
        }
        return rappels;
    }
}
