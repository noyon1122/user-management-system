package com.noyon.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.noyon.main.entity.OurUsers;

@Repository
public interface UsersRepo extends JpaRepository<OurUsers, Integer>
{
	Optional<OurUsers>findByEmail(String email);
}