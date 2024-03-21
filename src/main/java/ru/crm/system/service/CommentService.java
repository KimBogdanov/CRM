package ru.crm.system.service;

import org.springframework.stereotype.Service;
import ru.crm.system.database.entity.Comment;

import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;

@Service
public class CommentService {

    public Comment createComment(String text) {
        return Comment.of(text, now().truncatedTo(ChronoUnit.SECONDS));
    }
}