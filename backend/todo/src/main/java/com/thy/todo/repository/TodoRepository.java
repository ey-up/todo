package com.thy.todo.repository;

import com.thy.todo.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByIdAndUserId(Long todoId, Long userId);

    List<Todo> findAllByUserId(Long todoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Todo t WHERE t.id = :todoId AND t.user.id = :userId")
    void deleteByIdAndUserId(Long todoId, Long userId);


    Page<Todo> findByUserId(Long userId, Pageable pageable);


    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId AND LOWER(t.task) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Todo> findByUserId(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);


//    long countByUserId(Long userId);
//    @Query("SELECT COUNT(*) FROM Todo t WHERE t.user.id = :userId")
//    long countByIdAndStatus(Long userId);

//    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId AND LOWER(t.task) LIKE LOWER(CONCAT('%', :search, '%'))")
//    List<Todo> findByUserId(Long userId, String search);

//    List<Todo> findByUserId(Long userId, Pageable pageable);

//    @Query("SELECT t FROM Todo t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
//    List<Todo> findByTitleContaining(@Param("title") String title);
}
