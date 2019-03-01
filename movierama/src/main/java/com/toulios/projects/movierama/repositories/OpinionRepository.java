package com.toulios.projects.movierama.repositories;

import com.toulios.projects.movierama.model.Opinion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {

    @Query("SELECT o FROM Opinion o where o.user.id = :userId and o.movie.id in :movieIds")
    List<Opinion> findByUserIdAndMoviedIn(@Param("userId") Long userId, @Param("movieIds") List<Long> movieIds);

    Opinion findByUserIdAndMovieId(Long userId, Long movieId);

    @Query("SELECT o.movie.id FROM Opinion o WHERE o.user.id = :userId")
    Page<Long> findSelectedMovieIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(o.id) from Opinion o where o.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}
