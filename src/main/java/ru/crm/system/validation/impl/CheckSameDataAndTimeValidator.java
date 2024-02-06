package ru.crm.system.validation.impl;

import lombok.RequiredArgsConstructor;
import ru.crm.system.database.repository.LessonRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.validation.CheckSameDataAndTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class CheckSameDataAndTimeValidator implements ConstraintValidator<CheckSameDataAndTime, LessonCreateEditDto> {

    private final LessonRepository lessonRepository;

    @Override
    public boolean isValid(LessonCreateEditDto lessonDto, ConstraintValidatorContext context) {
        var maybeLesson = lessonRepository.findByDataAndTime(lessonDto.date(), lessonDto.time());
        return maybeLesson.isEmpty();
    }
}