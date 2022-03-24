package com.gng.springboot.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gng.springboot.commons.constant.Constants.UserStatus;
import com.gng.springboot.commons.exception.custom.AuthenticationException;
import com.gng.springboot.commons.exception.custom.UserExistException;
import com.gng.springboot.commons.exception.model.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.user.model.UserLoginDto;
import com.gng.springboot.user.model.UserRegisterDto;
import com.gng.springboot.user.model.UserEntity;
import com.gng.springboot.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * Load user by username(userId)
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		log.debug("Load user by [userId={}]", userId);
		
		return userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
	}
	
	/**
	 * Register user
	 * @param userRegisterDto
	 * @return
	 */
	public ResponseDto<String> registerUser(UserRegisterDto userRegisterDto) {
		ResponseDto<String> responseDto = new ResponseDto<>();
		
		try {
			UserEntity userEntity = UserEntity.builder()
					.userId(userRegisterDto.getUserId())
					.userName(userRegisterDto.getUserName())
					.userPwd(passwordEncoder.encode(userRegisterDto.getUserPwd()))
					.userStatus(UserStatus.USE.getStatus())
					.build();
			
			userRepository.findByUserId(userEntity.getUserId()).ifPresent(user -> {
				throw new UserExistException("동일한 아이디를 가진 사용자가 존재합니다.");
			});
			
			String id = userRepository.save(userEntity).getUserId();
			
			responseDto.set(ResponseCode.CREATE_SUCCESS, id);
		} catch(UserExistException ex) {
			responseDto.set(ResponseCode.CREATE_FAILURE, ex);
		}
		
		return responseDto;
	}
	
	/**
	 * Login user with user id/password
	 * @param userLoginDto
	 * @return
	 */
	public ResponseDto<UserEntity> loginUser(UserLoginDto userLoginDto) {
		ResponseDto<UserEntity> responseDto = new ResponseDto<>();

		try {
			log.trace("test");
			log.debug("test");
			log.info("test");
			log.warn("test");
			log.error("test");
			log.debug("TESTPWD [{}] [{}] [{}]", userLoginDto, userLoginDto.getUserPwd(), passwordEncoder.encode(userLoginDto.getUserPwd()));
			
			// Get user data with userId/userPw(Encrypted)
			UserEntity userEntity = userRepository.findByUserIdAndUserPwd(
					userLoginDto.getUserId(),
					passwordEncoder.encode(userLoginDto.getUserPwd())
			).orElseThrow(() -> new AuthenticationException("아이디 또는 비밀번호가 일치하지 않습니다."));
			
			// Remove encrypted password from response
			userEntity.setUserPwd("");
			
			responseDto.set(ResponseCode.LOGIN_SUCCESS, userEntity);
		} catch(AuthenticationException ex) {
			responseDto.set(ResponseCode.LOGIN_FAILURE, ex);
		}
		
		return responseDto;
	}
}
