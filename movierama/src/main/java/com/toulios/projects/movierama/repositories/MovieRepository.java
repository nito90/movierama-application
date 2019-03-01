package com.toulios.projects.movierama.repositories;

import com.toulios.projects.movierama.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findByUserId(Long userId, Pageable pageable);

    List<Movie> findByIdIn(List<Long> pollIds, Sort sort);

    long countByUserId(Long userId);
}
