package com.rabbit.manager.service;

import java.util.List;
import com.rabbit.manager.dto.UserDTO;
import com.rabbit.manager.exception.NotFoundException;
import com.rabbit.manager.model.User;

public interface UserService {
	
	UserDTO updateUser(UserDTO userDTO);
	List<UserDTO> allUser();
	UserDTO getByUsername(String username);
	UserDTO getById(String id);
	UserDTO softDelete(UserDTO userDTO);
	void delete(String id);
	UserDTO convertEntityToDto(User user);
	User convertDtoToEntity(UserDTO userDTO);
	UserDTO convertToDTO(User user);
	User convertToEntity(UserDTO userDTO);
	User addRoleToUser(String username, List<String> name) throws NotFoundException;
	User saveUser(User user);
	
}
