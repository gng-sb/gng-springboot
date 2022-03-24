package com.gng.springboot.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gng.springboot.commons.constant.Constants.UserStatus;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.util.PasswordEncryptionUtil;
import com.gng.springboot.user.model.UserEntity;
import com.gng.springboot.user.model.UserLoginDto;
import com.gng.springboot.user.model.UserRegisterDto;
import com.gng.springboot.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * User service
 * @author gchyoo
 *
 */
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
	public String registerUser(UserRegisterDto userRegisterDto) {
		UserEntity userEntity = UserEntity.builder()
				.userId(userRegisterDto.getUserId())
				.userPwd(passwordEncryptionUtil.encrypt(userRegisterDto.getUserPwd()))
				.userName(userRegisterDto.getUserName())
				.userStatus(UserStatus.USE.getStatus())
				.build();
		
		// Get user data with userId and check conflict
		userRepository.findByUserId(userEntity.getUserId()).ifPresent(user -> {
			throw new BusinessException(ResponseCode.USER_ID_CONFLICT);
		});
		
		// Insert registeration data
		String id = userRepository.save(userEntity).getUserId();
		
		return id;
	}
	
	/**
	 * Login user with user id/password
	 * @param userLoginDto
	 * @return
	 */
	public UserLoginDto loginUser(UserLoginDto userLoginDto) {
		// Get user data with userId
		UserEntity userEntity = userRepository.findByUserId(userLoginDto.getUserId())
				.orElseThrow(() -> new BusinessException(ResponseCode.USER_ID_FAILURE));
		
		if(!passwordEncryptionUtil.isValidUserPwd(userEntity.getUserPwd(), userLoginDto.getUserPwd())) {
			throw new BusinessException(ResponseCode.PASSWORD_FAILURE);
		}
		
		return UserLoginDto.of(userEntity);
	}
}
