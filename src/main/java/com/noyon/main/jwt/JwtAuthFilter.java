package com.noyon.main.jwt;
import java.io.IOException; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.noyon.main.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	

	private final JwtTokenUnit jwtTokenUnit;

	private final UserService userService;
	
	
	public JwtAuthFilter(JwtTokenUnit jwtTokenUnit, UserService userService) {
		super();
		this.jwtTokenUnit = jwtTokenUnit;
		this.userService = userService;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader=request.getHeader("Authorization");
		
		if(authHeader ==null || !authHeader.startsWith("Bearer "))
		{
			System.out.println("From authheader null in jwtAuthFilter class");
			filterChain.doFilter(request, response);
			return;
		}
		
		String token=authHeader.substring(7);
		String username=jwtTokenUnit.extractUsername(token);
		
		if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails userDetails=userService.loadUserByUsername(username);
			
			if(jwtTokenUnit.isValid(token, userDetails))
				
			{
				
				UsernamePasswordAuthenticationToken authenticationToken=new
						UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				authenticationToken.setDetails(
						  new WebAuthenticationDetailsSource().buildDetails(request)
						
						);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		filterChain.doFilter(request, response);
		

		
	}

}
