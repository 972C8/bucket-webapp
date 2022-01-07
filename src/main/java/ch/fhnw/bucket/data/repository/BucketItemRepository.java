/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BucketItemRepository extends JpaRepository<BucketItem, Long> {
    List<BucketItem> findByIdAndAvatarId(Long itemId, Long avatarId);
/*
    @Query("SELECT b FROM bucketItem b WHERE "
            + "(:avatarId = '' OR c.name LIKE concat('%', :name ,'%'') AND (:country = '' OR c.country LIKE concat ('%', :country, '%'')")
    List<BucketItem> findByParams(@Param("avatarId") Long avatarId, @Param("bucketId") Long bucketId);
*/

    /*
    Query concept from:
    https://www.baeldung.com/spring-data-jpa-null-parameters
    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

    The query enables optional parameters to be used. A variety of query params can be provided through the api
    that are optional due to the @Query code below.
     */

    //TODO: labelId, limit, sort(asc/desc)

    @Query("SELECT b FROM BucketItem b WHERE b.avatar.id = :avatarId AND (:bucketId IS NULL"
            + " OR b.bucket.id = :bucketId) AND (:completed IS NULL OR b.completed = :completed) ORDER BY b.created ASC")
    List<BucketItem> findByParams(
            @Param("avatarId") Long avatarId,
            @Param("bucketId") Long bucketId,
            @Param("completed") Boolean completed);
}