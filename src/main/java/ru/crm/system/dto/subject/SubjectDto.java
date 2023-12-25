package ru.crm.system.dto.subject;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для выдачи на фронт с id
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectDto {

    Integer id;

    String name;

    List<Student> students = new ArrayList<>();

    List<Teacher> teachers = new ArrayList<>();
}