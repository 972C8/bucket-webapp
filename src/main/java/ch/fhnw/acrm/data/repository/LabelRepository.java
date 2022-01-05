package ch.fhnw.acrm.data.repository;

import ch.fhnw.acrm.data.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findByAvatarId(Long labelId);
    List<Label> findByIdAndAvatarId(Long labelId, Long avatarId);
}