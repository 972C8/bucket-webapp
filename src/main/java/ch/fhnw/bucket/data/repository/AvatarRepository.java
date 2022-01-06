/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
	Avatar findAvatarById(Long avatarId);
	Avatar findByEmail(String email);
	Avatar findByEmailAndIdNot(String email, Long avatarId);
}
