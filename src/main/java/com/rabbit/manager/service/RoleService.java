package com.rabbit.manager.service;

import java.util.List;
import com.rabbit.manager.dto.RoleDTO;
import com.rabbit.manager.model.Role;

public interface RoleService {
	
	RoleDTO addRole(RoleDTO roleDTO);
	RoleDTO updateRole(RoleDTO roleDTO);
	List<RoleDTO> allRole();
	void deleteRole(String id);
	RoleDTO getById(String id);
	RoleDTO getByName(String name);
	RoleDTO convertEntityToDto(Role role);
	Role convertDtoToEntity(RoleDTO roleDTO);

}
