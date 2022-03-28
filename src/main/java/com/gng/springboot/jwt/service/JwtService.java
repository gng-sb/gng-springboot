package com.gng.springboot.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gng.springboot.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Jwt service
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService implements UserDetailsService {
	
	private final UserRepository userRepository;

	/**
	 * Load user by username(userId)
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		log.debug("Load user by [userId={}]", userId);
		
		return userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
	}
}
