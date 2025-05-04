package ru.job4j.tracker.mapstruct;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper {
    @Mapping(target = "className", source = "classVal")
    StudentDto getModelFromEntity(StudentEntity student);
    @InheritInverseConfiguration
    StudentEntity getEntityFromDto(StudentDto studentDto);

    default StudentDto getModelFromEntityCustom(StudentEntity studentEntity) {
        StudentDto student = new StudentDto();
        student.setId(studentEntity.getId());
        student.setName(studentEntity.getName());
        student.setClassName(studentEntity.getClassVal());
        return student;
    }
}
