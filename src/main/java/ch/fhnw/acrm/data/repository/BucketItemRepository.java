/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.acrm.data.repository;

import ch.fhnw.acrm.data.domain.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BucketItemRepository extends JpaRepository<BucketItem, Long> {
	List<BucketItem> findByAvatarId(Long avatarId);
	List<BucketItem> findByIdAndAvatarId(Long ItemId, Long avatarId);
}