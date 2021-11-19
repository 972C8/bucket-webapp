/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.acrm.data.repository;

import ch.fhnw.acrm.data.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Customer findByMobile(String mobile);
	Customer findByMobileAndIdNot(String mobile, Long avatarId);
	List<Customer> findByAvatarId(Long avatarId);
	List<Customer> findByIdAndAvatarId(Long customerId, Long avatarId);
}