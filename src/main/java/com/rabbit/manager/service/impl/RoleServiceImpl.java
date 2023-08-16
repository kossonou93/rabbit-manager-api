package com.rabbit.manager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.rabbit.manager.dto.RoleDTO;
import com.rabbit.manager.model.Role;
import com.rabbit.manager.repository.RoleRepository;
import com.rabbit.manager.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public RoleDTO addRole(RoleDTO userDTO) {
		return convertEntityToDto(roleRepository.save(convertDtoToEntity(userDTO)));
	}

	@Override
	public RoleDTO updateRole(RoleDTO userDTO) {
		return convertEntityToDto(roleRepository.save(convertDtoToEntity(userDTO)));
	}

	@Override
	public List<RoleDTO> allRole() {
		return roleRepository.findAll()
				.stream()
				.map(this::convertEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteRole(String id) {
		try {
			roleRepository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public RoleDTO getById(String id) {
		return convertEntityToDto(roleRepository.findById(id).get());
	}

	@Override
	public RoleDTO convertEntityToDto(Role role) {
		return modelMapper.map(role, RoleDTO.class);
	}

	@Override
	public Role convertDtoToEntity(RoleDTO roleDTO) {
		return modelMapper.map(roleDTO, Role.class);
	}

	@Override
	public RoleDTO getByName(String name) {
		return convertEntityToDto(roleRepository.findByName(name).get());
	}

}
