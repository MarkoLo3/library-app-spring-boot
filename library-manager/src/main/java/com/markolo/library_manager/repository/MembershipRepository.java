package com.markolo.library_manager.repository;

import com.markolo.library_manager.model.Membership;
import com.markolo.library_manager.model.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByUserId(Long userId);

    Optional<Membership> findByUserIdAndStatus(Long userId, MembershipStatus status);

    void deleteByUserId(Long userId);
}
