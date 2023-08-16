package com.rabbit.manager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.rabbit.manager.dto.UserDTO;
import com.rabbit.manager.exception.NotFoundException;
import com.rabbit.manager.model.Role;
import com.rabbit.manager.model.User;
import com.rabbit.manager.repository.RoleRepository;
import com.rabbit.manager.repository.UserRepository;
import com.rabbit.manager.service.RoleService;
import com.rabbit.manager.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Transactional
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
	
	@Override
	public User convertToEntity(UserDTO userDTO) {
        // Convertir le DTO UserDTO en entité User
        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if(userDTO.getStatus() == null) {
			user.setStatus(true);
		}
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        List<Role> existingRoles = new ArrayList<>();
        for (String role : userDTO.getRoles()) {
            Role existingRole = roleRepository.findByName(role).get();
            if (existingRole == null) {
                throw new IllegalArgumentException("Le rôle " + role + " n'existe pas.");
            }
            existingRoles.add(existingRole);
        }

        // Associer les rôles existants à l'utilisateur
        user.setRoles(existingRoles);
        return user;
    }
	
	@Override
	public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setStatus(user.getStatus());
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        userDTO.setRoles(roles);

        return userDTO;
    }

	@Override
	public UserDTO updateUser(UserDTO userDTO) {
		return convertEntityToDto(userRepository.save(convertDtoToEntity(userDTO)));
	}

	@Override
	public List<UserDTO> allUser() {
		return userRepository.findAll()
				.stream()
				.map(this::convertEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public UserDTO getByUsername(String username) {
		return userRepository.findByUsername(username)
				.stream()
				.map(this::convertEntityToDto)
				.collect(Collectors.toList())
				.stream()
				.findFirst()
				.orElse(null);
	}

	@Override
	public UserDTO getById(String id) {
		return convertEntityToDto(userRepository.findById(id).get());
	}

	@Override
	public UserDTO softDelete(UserDTO userDTO) {
		return convertEntityToDto(userRepository.save(convertDtoToEntity(userDTO)));
	}

	@Override
	public void delete(String id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public UserDTO convertEntityToDto(User user) {
		return modelMapper.map(user, UserDTO.class);
	}

	@Override
	public User convertDtoToEntity(UserDTO userDTO) {
		return modelMapper.map(userDTO, User.class);
	}
	
	@Override
	public User addRoleToUser(String username, List<String> roles) throws NotFoundException{
		User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found for this username :: " + username));
		List<Role> listRoles = new ArrayList<Role>();
		for(String r: roles) {
			listRoles.add(roleRepository.findByName(r).orElseThrow(() -> new NotFoundException("Role not found for this name :: " + r)));
		}
		user.setRoles(listRoles);
		userRepository.save(user);
		return user;
	}
	
	@Override
	public User saveUser(User user) {
		//user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

}
