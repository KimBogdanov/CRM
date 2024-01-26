package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.crm.system.database.entity.enums.OrderStatus;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = {"phone", "createdAt"}, callSuper = true)
@ToString(exclude = {"id", "admin", "logInfos", "comments"}, callSuper = true)
@Table(name = "orders")
@Entity
public class Order extends AbstractEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String orderName;

    private String clientName;

    @Column(nullable = false)
    private String phone;

    private String requestSource;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<LogInfo> logInfos = new ArrayList<>();

    @ManyToOne
    private Admin admin;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "comment")
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}