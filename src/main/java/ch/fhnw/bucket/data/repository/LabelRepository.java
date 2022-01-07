package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findByAvatarId(Long avatarId);
    List<Label> findByIdAndAvatarId(Long labelId, Long avatarId);
    Label findLabelByIdAndAvatarId(Long labelId, Long avatarId);
}