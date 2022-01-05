package ch.fhnw.acrm.data.repository;

import ch.fhnw.acrm.data.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
}