package com.noyon.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.noyon.main.entity.Token;

public interface TokenRepo extends JpaRepository<Token, Integer> {

	Optional<Token>findByToken(String token);
	
	@Query("""
			
			select t from Token t inner join OurUsers u on t.user.id=u.id
			where t.user.id= :userid and t.logout=false
			
			""")
	List<Token>findAllTokenByUserId(int userid);
}
