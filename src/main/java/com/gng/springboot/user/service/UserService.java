package com.gng.springboot.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gng.springboot.commons.constant.Constants.UserStatus;
import com.gng.springboot.commons.exception.custom.AuthenticationException;
import com.gng.springboot.commons.exception.custom.UserExistException;
import com.gng.springboot.commons.exception.model.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.commons.util.PasswordEncryptionUtil;
import com.gng.springboot.user.model.UserEntity;
import com.gng.springboot.user.model.UserLoginDto;
import com.gng.springboot.user.model.UserRegisterDto;
import com.gng.springboot.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final PasswordEncryptionUtil passwordEncryptionUtil;
	
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
					.userPwd(passwordEncryptionUtil.encrypt(userRegisterDto.getUserPwd()))
					.userName(userRegisterDto.getUserName())
					.userStatus(UserStatus.USE.getStatus())
					.build();
			
			// Get user data with userId and check conflict
			userRepository.findByUserId(userEntity.getUserId()).ifPresent(user -> {
				throw new UserExistException("동일한 아이디를 가진 사용자가 존재합니다.");
			});
			
			// Insert registeration data
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
			// Get user data with userId
			UserEntity userEntity = userRepository.findByUserId(userLoginDto.getUserId())
					.orElseThrow(() -> new AuthenticationException("아이디가 존재하지 않습니다."));
			
			if(!passwordEncryptionUtil.isValidUserPwd(userEntity.getUserPwd(), userLoginDto.getUserPwd())) {
				throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
			}
			
			// Remove encrypted password from response
			userEntity.setUserPwd("");
			
			responseDto.set(ResponseCode.LOGIN_SUCCESS, userEntity);
		} catch(AuthenticationException ex) {
			responseDto.set(ResponseCode.LOGIN_FAILURE, ex);
		}
		
		return responseDto;
	}
}
