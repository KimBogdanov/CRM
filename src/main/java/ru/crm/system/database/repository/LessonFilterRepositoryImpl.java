package ru.crm.system.database.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.querydsl.QueryDslPredicate;
import ru.crm.system.dto.filter.LessonFilter;
import ru.crm.system.dto.filter.MonthSchedule;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static ru.crm.system.database.entity.QLesson.lesson;

@RequiredArgsConstructor
public class LessonFilterRepositoryImpl implements LessonFilterRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Lesson> findAllByFilter(LessonFilter lessonFilter) {
        Predicate predicates;
        if (lessonFilter.month() != null) {
            predicates = QueryDslPredicate.builder()
                    .add(lessonFilter.from(), lesson.date::goe)
                    .add(lessonFilter.to(), lesson.date::loe)
                    .add(lessonFilter.month().getMonth().getValue(), lesson.date.month()::eq)
                    .add(MonthSchedule.year.getValue(), lesson.date.year()::eq)
                    .build();
        } else {
            predicates = QueryDslPredicate.builder()
                    .add(lessonFilter.from(), lesson.date::goe)
                    .add(lessonFilter.to(), lesson.date::loe)
                    .build();
        }

        return new JPAQuery<Lesson>(entityManager)
                .select(lesson)
                .from(lesson)
                .where(predicates)
                .orderBy(lesson.date.asc())
                .fetch();
    }
}