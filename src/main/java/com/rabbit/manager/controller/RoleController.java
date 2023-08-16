package com.rabbit.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.manager.dto.RoleDTO;
import com.rabbit.manager.service.RoleService;
import com.rabbit.manager.utility.ApiResponse;

@RestController
@RequestMapping(value = "/role")
@CrossOrigin
public class RoleController {

	@Autowired
	RoleService roleService;
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@GetMapping("/all")
	public ApiResponse<List<RoleDTO>> getAllRoles(){
		logger.info("Request received to get all roles");
		try {
			List<RoleDTO> roles = roleService.allRole();
			return new ApiResponse<>(true, "Roles found successfully", roles);
		} catch (Exception e) {
			logger.error("Error while retrieving all roles");
			return new ApiResponse<>(false, "Failed to get all roles", null);
		}
		
	}
	
	@PostMapping("/add")
	public ApiResponse<RoleDTO> addRole(@RequestBody RoleDTO roleDTO){
		logger.info("Request to add role");
		try {
			return new ApiResponse<>(true, "Role added successfully", roleService.addRole(roleDTO));
		} catch (Exception e) {
			return new ApiResponse<>(false, "Failed to add role", null);
		}
	}
	
	@GetMapping("/{id}")
	public ApiResponse<RoleDTO> getRoleById(@PathVariable String id){
	    logger.info("Request received to get role with ID: {}", id);
	    try {
	        RoleDTO roleDTO = roleService.getById(id);
	        logger.info("Role found successfully: {}", roleDTO);
	        return new ApiResponse<>(true, "Role found successfully", roleDTO);
	    } catch (Exception ex) {
	        logger.error("Error while retrieving role with ID: {}", id, ex);
	        return new ApiResponse<>(false, "Failed to get role", null);
	    }
	}
	
	@GetMapping("/{name}")
	public ApiResponse<RoleDTO> getRoleByName(@PathVariable String name){
	    logger.info("Request received to get role with name: {}", name);
	    try {
	        RoleDTO roleDTO = roleService.getByName(name);
	        logger.info("Role found successfully: {}", roleDTO);
	        return new ApiResponse<>(true, "Role found successfully", roleDTO);
	    } catch (Exception ex) {
	        logger.error("Error while retrieving role with name: {}", name, ex);
	        return new ApiResponse<>(false, "Failed to get role", null);
	    }
	}
	
	@PutMapping("/{id}")
    public ApiResponse<RoleDTO> putRole(@PathVariable String id, @RequestBody RoleDTO role) {
        logger.info("Request received to update role with ID: {}", id);
        try {
            RoleDTO roleDTO = roleService.getById(id);
            roleDTO.setName(role.getName());
            logger.info("Role update successfully with ID: {}", id);
            return new ApiResponse<>(true, "Role update successfully", roleService.updateRole(roleDTO));
        } catch (Exception ex) {
            logger.error("Error while updating role with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to update role", null);
        }
    }
	
	
	@DeleteMapping("/delete/{id}")
	public ApiResponse<RoleDTO> deleteRole(@PathVariable String id){
		logger.info("Request delete role with ID: {}", id);
        try {
            roleService.deleteRole(id);
            return new ApiResponse<>(true, "Role delete successfully", null);
        } catch (Exception ex) {
            logger.error("Error while soft deleting role with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to soft delete role", null);
        }
	}
}
