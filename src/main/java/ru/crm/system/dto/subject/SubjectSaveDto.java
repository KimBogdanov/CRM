package ru.crm.system.dto.subject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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

//    List<Student> students = new ArrayList<>();
//
//    List<Teacher> teachers = new ArrayList<>();
}