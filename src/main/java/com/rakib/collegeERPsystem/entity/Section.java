package com.rakib.collegeERPsystem.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
public class Section extends BaseEntity{

    private String sectionName;

    @ManyToOne
    private Classes classes;

    @OneToMany(mappedBy = "section")
    private List<Student> students;
}
