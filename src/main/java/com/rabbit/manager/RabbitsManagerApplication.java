package com.rabbit.manager;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import com.rabbit.manager.dto.RoleDTO;
import com.rabbit.manager.dto.UserDTO;
import com.rabbit.manager.exception.NotFoundException;
import com.rabbit.manager.repository.RoleRepository;
import com.rabbit.manager.repository.UserRepository;
import com.rabbit.manager.service.RoleService;
import com.rabbit.manager.service.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class RabbitsManagerApplication {
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(RabbitsManagerApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@PostConstruct
	public void init() throws NotFoundException{
		createRoleUser();
		createRoleAdmin();
		createAdmin();
	}
	
	public void createRoleUser() {
		RoleDTO roleDTO = new RoleDTO();
		if(roleRepository.findByName("USER").isPresent()) {
		}else {
			roleDTO.setName("USER");
			roleService.addRole(roleDTO);
		}
	}
	
	public void createRoleAdmin() {
		RoleDTO roleDTO = new RoleDTO();
		if(roleRepository.findByName("ADMIN").isPresent()) {
		}else {
			roleDTO.setName("ADMIN");
			roleService.addRole(roleDTO);
		}
	}
	
	public void createAdmin() throws NotFoundException{
		UserDTO userDTO = new UserDTO();
		if(userRepository.findByUsername("admin").isPresent()) {
		}else {
			userDTO.setEmail("admin@gmail.com");
			userDTO.setUsername("admin");
			userDTO.setName("Admin");
			userDTO.setPassword("admin");
			userDTO.setStatus(true);
			List<String> roles = new ArrayList<>();
			roles.add("ADMIN");
			userDTO.setRoles(roles);
			System.out.println("roles ==> " + roleRepository.findByName(roles.get(0)));
			userService.saveUser(userService.convertToEntity(userDTO));
			userService.addRoleToUser(userDTO.getUsername(), roles);
		}
	}

}
