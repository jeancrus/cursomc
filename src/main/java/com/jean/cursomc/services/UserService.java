package com.jean.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.jean.cursomc.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //retorna o usuário logado
			//não tiver usuário logado, pelo catch retorna null
		}
		catch (Exception e) {
			return null;
		}
	}

}
