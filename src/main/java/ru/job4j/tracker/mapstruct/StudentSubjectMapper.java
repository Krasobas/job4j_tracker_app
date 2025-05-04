package ru.job4j.tracker.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentSubjectMapper {
    @Mapping(target = "className", source = "classVal")
    @Mapping(target = "subject", source = "subject.name")
    StudentSubjectDto getModelFromEntity(StudentSubject studentSubject);
}
