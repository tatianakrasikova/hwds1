package ait.hwds.service.interfaces;



import ait.hwds.model.dto.CreateOrUpdateDepartamentDto;
import ait.hwds.model.dto.DepartamentDto;
import ait.hwds.model.entity.Departament;

import java.math.BigDecimal;
import java.util.List;

public interface DepartamentService {

    List<DepartamentDto> getAllDepartaments();

    DepartamentDto getDepartamentById(Long id);

    DepartamentDto createDepartament(CreateOrUpdateDepartamentDto departamentDto);

    void deleteDepartament(Long id);

    Departament findByIdOrThrow(Long id);

    BigDecimal getTotalArticlePriceForDepartament(Long departamentId);

    BigDecimal getTotalArticlePriceForDepartament(Departament departament);

    List<DepartamentDto> getAllDepartament();
}
