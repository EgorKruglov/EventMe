package ru.practicum.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.EventCommentsCount;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c " +
            "from Comment as c " +
            "where lower(c.text) like lower(concat('%', ?1, '%') )")
    List<Comment> findCommentListByText(String text, Pageable pageable);

    @Query("select new ru.practicum.comment.model.dto.EventCommentsCount(e.id, COUNT(c)) " +
            "from Event e left join Comment c on e.id = c.event.id " +
            "where e.id in ?1 " +
            "GROUP BY e.id")
    List<EventCommentsCount> countCommentByEvent(List<Long> eventIds);

    List<Comment> findAllByEventId(long eventId, Pageable pageable);

    Optional<Comment> findByAuthorIdAndId(Long userId, Long id);

    List<Comment> findByAuthorId(Long userId, PageRequest pageable);
}
