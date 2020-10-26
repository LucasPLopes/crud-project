package br.com.challenge.api.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.gson.Gson;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;

		setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String body = "";
		Authentication authenticationToken = null;
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			try {
				body = request.getReader().lines().collect(Collectors.joining());
				UserLogin userLogin = new Gson().fromJson(body, UserLogin.class);
				authenticationToken = new UsernamePasswordAuthenticationToken(userLogin.username, userLogin.password);
                System.out.println(userLogin.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain, Authentication authentication) {
		User user = ((User) authentication.getPrincipal());

		List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

		String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
				.setHeaderParam("typ", SecurityConstants.TOKEN_TYPE).setIssuer(SecurityConstants.TOKEN_ISSUER)
				.setAudience(SecurityConstants.TOKEN_AUDIENCE).setSubject(user.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + 864000000)).claim("rol", roles).compact();

		ResponseLogin body = new ResponseLogin();
		body.token = token;
		PrintWriter out;

		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(new Gson().toJson(body));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class UserLogin {

		String username;
        String password;

        @Override
        public String toString() {
            return super.toString();
        }
	}

	class ResponseLogin {
		String token;

	}

}
