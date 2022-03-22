package com.gng.springboot.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gng.springboot.commons.constant.Constants.UserStatus;
import com.gng.springboot.commons.exception.model.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.jwt.model.GngUserDto;
import com.gng.springboot.jwt.model.GngUserEntity;
import com.gng.springboot.jwt.repository.JwtUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUserService implements UserDetailsService {
	private final JwtUserRepository jwtUserRepository;
	
	/**
	 * Load user by username(userId)
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		log.debug("Load user by [userId={}]", userId);
		
		return jwtUserRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
	}
	
	/**
	 * Register user
	 * @param gngUserDto
	 * @return
	 */
	public ResponseDto<String> registerUser(GngUserDto gngUserDto) {
		ResponseDto<String> responseDto = new ResponseDto<>();
		
		try {
			GngUserEntity gngUserEntity = GngUserEntity.builder()
					.userId(gngUserDto.getUserId())
					.userName(gngUserDto.getUserName())
					.userPwd(gngUserDto.getUserPwd())
					.userStatus(UserStatus.USE.getStatus())
					.build();
			String id = jwtUserRepository.save(gngUserEntity).getUserId();
			log.debug(id);
			responseDto.set(ResponseCode.CREATE_SUCCESS, id);
		} catch(Exception ex) {
			responseDto.set(ResponseCode.CREATE_FAILURE);
		}
		
		return responseDto;
	}
}
