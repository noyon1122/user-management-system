package com.noyon.main.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noyon.main.dto.AuthenticationResponse;
import com.noyon.main.entity.OurUsers;
import com.noyon.main.entity.Role;
import com.noyon.main.entity.Token;
import com.noyon.main.jwt.JwtTokenUnit;
import com.noyon.main.repository.TokenRepo;
import com.noyon.main.repository.UsersRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthService {

	@Autowired
	private UsersRepo usersRepo;
	
	@Autowired
	private TokenRepo tokenRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenUnit jwtTokenUnit;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//save the token information
	public void saveUserToken(String jwt,OurUsers user)
	{
		Token token=new Token();
		token.setToken(jwt);
		token.setLogout(false);
		token.setOurUsers(user);
		tokenRepo.save(token);
	}
	
	// inActive all logout token
	
	public void inActiveAllLogoutToken(OurUsers user)
	{
		List<Token> validTokens=tokenRepo.findAllTokenByUserId(user.getId());
		
		if(validTokens.isEmpty())
		{
			return;
		}
		else {
			validTokens.forEach(token-> token.setLogout(false));
		}
		tokenRepo.saveAll(validTokens);
	}
	
	//register 
	public AuthenticationResponse register(OurUsers user)
	{
		if(usersRepo.findByEmail(user.getEmail()).isPresent())
		{
			return new AuthenticationResponse(null,"User already exists for this email");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.valueOf("ADMIN"));
		usersRepo.save(user);
		
		String jwt=jwtTokenUnit.generatedToken(user);
		saveUserToken(jwt, user);
		//send activation will be added later
		
		return new AuthenticationResponse(jwt,"User Registration Successfully!");
		
		
	}
	
	//send activation mail later will be added
	
	public AuthenticationResponse login(OurUsers request)
	{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
					);
			OurUsers user=usersRepo.findByEmail(request.getEmail()).orElseThrow();
			
			String jwt=jwtTokenUnit.generatedToken(user);
			
			return new AuthenticationResponse(jwt,"User Login Successfully!!");
		} catch (Exception e) {
			// TODO: handle exception
			return new AuthenticationResponse(null,"Some thing went wrong ,Try again with valid Email and Password");
		}
		
	
	}
	
	//get all user
	
	public List<OurUsers> getAllUsers()
	{
			List<OurUsers>allUsers=usersRepo.findAll();
		    return allUsers;
	}
	
	//get user by id
	
	public OurUsers getOurUserById(int id)
	{
		OurUsers user=usersRepo.findById(id).orElseThrow(
				()-> new EntityNotFoundException("User not found by This id :"+id)
				);
		return user;
	}
	
	//update user by id
	public OurUsers updateUser(OurUsers updateuser,int id)
	{
		OurUsers existinguser=usersRepo.findById(id).orElseThrow(
				()-> new EntityNotFoundException("User not found for this id :"+id)
				);
	
			existinguser.setCity(updateuser.getCity());
			existinguser.setName(updateuser.getName());
			existinguser.setPassword(updateuser.getPassword());
			existinguser.setName(updateuser.getName());
			
		usersRepo.save(existinguser);
		return updateuser;
	}
	
	//get user by email
	public OurUsers getMyProfile(String email)
	{
		OurUsers user=usersRepo.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User not found by this email :"+email)
				);
			return user;
	}
	
	//delete User
	
	public void deleteuser(int id) {
		usersRepo.deleteById(id);
	}
	
}
