package com.gng.springboot.user.service;

import org.springframework.stereotype.Service;

import com.gng.springboot.commons.constant.Constants.RoleTypes;
import com.gng.springboot.commons.constant.Constants.UserStatusTypes;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.util.PasswordEncryptionUtil;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.user.model.UserEntity;
import com.gng.springboot.user.model.UserLoginDto;
import com.gng.springboot.user.model.UserRegisterDto;
import com.gng.springboot.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * User service
 * @author gchyoo
 *
 */
@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncryptionUtil passwordEncryptionUtil;
	private final JwtTokenProvider jwtTokenProvider;
	
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
				.userStatus(UserStatusTypes.USE.getStatus())
				.build();
		
		// Add role type(ROLE_USER)
		userEntity.addRoleType(RoleTypes.ROLE_USER);
		userEntity.addRoleType(RoleTypes.ROLE_ADMIN);
		
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
		
		// TODO: JWT create 무한루프 수정
		return UserLoginDto.of(userEntity, jwtTokenProvider.createToken(userEntity.getUserId(), userEntity.getRoleTypeSet()));
	}
}
