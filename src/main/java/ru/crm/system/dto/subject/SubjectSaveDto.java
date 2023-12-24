package ru.crm.system.dto.subject;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO приходит с фронта для сохранения в базу данных,
 * без id ,он присваивается базой данных
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectSaveDto {

    String name;

    List<Student> students = new ArrayList<>();

    List<Teacher> teachers = new ArrayList<>();
}