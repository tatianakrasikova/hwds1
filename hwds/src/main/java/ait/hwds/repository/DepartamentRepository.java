package ait.hwds.repository;


import ait.hwds.model.entity.Departament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentRepository extends JpaRepository<Departament, Long> {
}
