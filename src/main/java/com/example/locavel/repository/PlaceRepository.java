package com.example.locavel.repository;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Places, Long> {
    Page<Places> findAllByUser(User user, PageRequest of);
}
