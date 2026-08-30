package rs.ac.bg.fon.fonsledje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.fonsledje.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
