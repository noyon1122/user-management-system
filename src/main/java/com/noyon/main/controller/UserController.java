package com.noyon.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.noyon.main.dto.AuthenticationResponse;
import com.noyon.main.entity.OurUsers;
import com.noyon.main.service.AuthService;

@RestController
public class UserController {

	@Autowired
	private AuthService authService;
	
	
	//register
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> userRegister(@RequestBody OurUsers user)
	{
		return ResponseEntity.ok(authService.register(user));
		
	}
	
	//login 
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> userlogin(@RequestBody OurUsers user)
	{
		return ResponseEntity.ok(authService.login(user));
	}
	
	//get all user
	@GetMapping("/admin/users")
	public ResponseEntity<List<OurUsers>> getAllUsers()
	{
		return ResponseEntity.ok(authService.getAllUsers());
	}
	
	//get user by id
	@GetMapping("/admin/{id}")
	public ResponseEntity<OurUsers> getUserById(@PathVariable int id)
	{
		return ResponseEntity.ok(authService.getOurUserById(id));
	}
	
	//get user by id
	@PutMapping("/admin/update/{id}")
	public ResponseEntity<OurUsers> updateUserById(@RequestBody OurUsers user, @PathVariable int id)
	{
		return ResponseEntity.ok(authService.updateUser(user, id));
	}
	
	//get profile
	@GetMapping("/adminuser/profiles/{email}")
	public ResponseEntity<OurUsers> getProfile(@PathVariable String email)
	{
        return ResponseEntity.ok(authService.getMyProfile(email));
	}
	
	@DeleteMapping("admin/delete/{id}")
	public ResponseEntity<String> deleteuser(@PathVariable int id)
	{    authService.deleteuser(id);
		return ResponseEntity.ok("User deleted Successfully!");
	}
	
	
}
