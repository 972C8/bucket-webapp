/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BucketItemRepository extends JpaRepository<BucketItem, Long> {
	List<BucketItem> findByAvatarId(Long avatarId);
	List<BucketItem> findByIdAndAvatarId(Long ItemId, Long avatarId);
	BucketItem findBucketItemByIdAndAvatarId(Long bucketId, Long avatarId);
}