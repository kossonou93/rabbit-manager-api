package com.rabbit.manager.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rabbit.manager.model.User;
import com.rabbit.manager.repository.RoleRepository;
import com.rabbit.manager.repository.UserRepository;
import com.rabbit.manager.exception.NotFoundException;
import com.rabbit.manager.security.JwtResponse;
import com.rabbit.manager.dto.UserDTO;
import com.rabbit.manager.security.JwtUtils;
import com.rabbit.manager.service.RoleService;
import com.rabbit.manager.service.UserService;
import com.rabbit.manager.utility.ApiResponse;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@PostMapping("/login")
	public ApiResponse<?> authenticateUser(@RequestBody UserDTO loginRequest) throws NotFoundException{
		// Authentifier l'utilisateur
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    loginRequest.getUsername(),
	                    loginRequest.getPassword()
	            )
	    );
	    User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new NotFoundException("User not found for this username :: " + loginRequest.getUsername()));
	    // Créer le token JWT
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    // Renvoyer le token JWT
	    return new ApiResponse<>(true, "User Authenticated successfully.", new JwtResponse(jwt, user));
	}

	
	@GetMapping("/all")
	public ApiResponse<List<UserDTO>> getAllUsers(){
		logger.info("Request received to get all users");
		try {
			List<UserDTO> users = userService.allUser();
			return new ApiResponse<>(true, "Users found successfully", users);
		} catch (Exception e) {
			logger.error("Error while retrieving all users");
			return new ApiResponse<>(false, "Failed to get all users", null);
		}
		
	}
	
	// Add User
	@PostMapping("/add")
	public ApiResponse<UserDTO> addUser(@RequestBody UserDTO userDTO) throws NotFoundException {
		
		if(userRepository.findByUsername(userDTO.getUsername()).isPresent() || userRepository.findByUsername(userDTO.getEmail()).isPresent()) {
			logger.error("Failed email or username exist !!!");
			return new ApiResponse<>(true, "Failed email or username exist !!!", null);
		}else {
			userService.saveUser(userService.convertToEntity(userDTO));
			User usr = userService.addRoleToUser(userDTO.getUsername(), userDTO.getRoles());
	        return new ApiResponse<>(true, "User save Successfully.", userService.convertToDTO(usr));
		}
	}
	
	@GetMapping("/{id}")
	public ApiResponse<UserDTO> getUserById(@PathVariable String id){
	    logger.info("Request received to get user with ID: {}", id);
	    try {
	        UserDTO userDTO = userService.getById(id);
	        logger.info("User found successfully: {}", userDTO);
	        return new ApiResponse<>(true, "User found successfully", userDTO);
	    } catch (Exception ex) {
	        logger.error("Error while retrieving user with ID: {}", id, ex);
	        return new ApiResponse<>(false, "Failed to get user", null);
	    }
	}
	
	@GetMapping("/{username}")
	public ApiResponse<UserDTO> getUserByUsername(@PathVariable String username){
	    logger.info("Request received to get user with ID: {}", username);
	    try {
	        UserDTO userDTO = userService.getByUsername(username);
	        logger.info("User found successfully: {}", userDTO);
	        return new ApiResponse<>(true, "User found successfully", userDTO);
	    } catch (Exception ex) {
	        logger.error("Error while retrieving user with ID: {}", username, ex);
	        return new ApiResponse<>(false, "Failed to get user", null);
	    }
	}
	
	@PutMapping("/{id}")
    public ApiResponse<UserDTO> putUser(@PathVariable String id, @RequestBody UserDTO user) {
        logger.info("Request received to update user with ID: {}", id);
        try {
            UserDTO userDTO = userService.getById(id);
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setUsername(user.getUsername());
            userDTO.setStatus(user.getStatus());
            userDTO.setPassword(user.getPassword());
            logger.info("User update successfully with ID: {}", id);
            return new ApiResponse<>(true, "User update successfully", userService.updateUser(userDTO));
        } catch (Exception ex) {
            logger.error("Error while updating user with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to update user", null);
        }
    }
	
	@PutMapping("/soft-delete/{id}")
    public ApiResponse<UserDTO> softDeleteUser(@PathVariable String id) {
        logger.info("Request soft deleted user with ID: {}", id);
        try {
            UserDTO userDTO = userService.getById(id);
            userDTO.setStatus(false);
            logger.info("User soft delete successfully with ID: {}", id);
            return new ApiResponse<>(true, "User soft delete successfully", userService.softDelete(userDTO));
        } catch (Exception ex) {
            logger.error("Error while soft deleting user with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to soft delete user", null);
        }
    }
	
	
	@DeleteMapping("/delete/{id}")
	public ApiResponse<UserDTO> deleteUser(@PathVariable String id){
		logger.info("Request delete user with ID: {}", id);
        try {
            userService.delete(id);
            return new ApiResponse<>(true, "User delete successfully", null);
        } catch (Exception ex) {
            logger.error("Error while soft deleting user with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to soft delete user", null);
        }
	}
}
