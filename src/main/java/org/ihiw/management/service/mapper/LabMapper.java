package org.ihiw.management.service.mapper;

import org.ihiw.management.domain.IhiwLab;
import org.ihiw.management.domain.User;
import org.ihiw.management.service.dto.LabDTO;
import org.ihiw.management.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class LabMapper {

    public List<LabDTO> labToLabDTO(List<IhiwLab> labs) {
        return labs.stream()
            .filter(Objects::nonNull)
            .map(this::labToLabDTO)
            .collect(Collectors.toList());
    }

    public LabDTO labToLabDTO(IhiwLab lab) {
        return new LabDTO(lab);
    }

}
