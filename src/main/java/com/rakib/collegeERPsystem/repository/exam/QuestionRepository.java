package com.rakib.collegeERPsystem.repository.exam;

import com.rakib.collegeERPsystem.entity.exam.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
