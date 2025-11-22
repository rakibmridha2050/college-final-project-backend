package com.rakib.collegeERPsystem.repository;
import com.rakib.collegeERPsystem.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByDepartmentId(Long departmentId);
    Faculty findByEmail(String email);
    long count();

}