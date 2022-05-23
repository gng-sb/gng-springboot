package com.gng.springboot.jwt.service;

import com.gng.springboot.jwt.model.JwtRefreshDto;

public interface JwtServiceImpl {
	public JwtRefreshDto refreshToken(JwtRefreshDto jwtRefreshDto);
}
