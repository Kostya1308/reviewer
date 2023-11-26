package ru.clevertec.courses.reviewer.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.courses.reviewer.entity.LaunchLine;

import java.util.List;

public interface LaunchLineRepository extends JpaRepository<LaunchLine, Integer> {

    @EntityGraph(attributePaths = "branch")
    List<LaunchLine> findByBranch_Id(Integer id);

}
