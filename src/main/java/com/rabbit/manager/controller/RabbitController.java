package com.rabbit.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rabbit.manager.dto.RabbitDTO;
import com.rabbit.manager.repository.RabbitRepository;
import com.rabbit.manager.service.RabbitService;
import com.rabbit.manager.utility.ApiResponse;

@RestController
@RequestMapping(value = "/rabbit")
@CrossOrigin
public class RabbitController {
	
	@Value("${rabbit.lentght}")
    private Integer rabbitLenght;
	
	@Autowired
	RabbitService rabbitService;
	
	@Autowired
	RabbitRepository rabbitRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitController.class);
	
	// Récupérer la liste des Lapins
	@GetMapping("/all")
	public ApiResponse<List<RabbitDTO>> getAllRabbits(){
		try {
			logger.info("Request received to get all rabbits");
			List<RabbitDTO> rabbits = rabbitService.allRabbit();
			logger.info("Request received to get all rabbits");
			return new ApiResponse<>(true, "Rabbits found successfully", rabbits);
		} catch (Exception e) {
			logger.error("Error while retrieving all rabbits" + e.getMessage());
			return new ApiResponse<>(false, "Failed to get all rabbits", null);
		}
		
	}
	
	// Ajouter un Lapin
	@PostMapping("/add")
	public ApiResponse<RabbitDTO> addRabbit(@RequestBody RabbitDTO rabbitDTO){
		logger.info("Request to add rabbit");
		if (rabbitLenght > rabbitService.countRabbit()) {
			logger.info("Rabbit can saving");
			if(!rabbitRepository.findByName(rabbitDTO.getName()).isEmpty()) {
				logger.info("Rabbit exist !!!");
				logger.info("Failed rabbit already exist: {}", rabbitDTO.getName());
				return new ApiResponse<>(false, "Failed rabbit already exist !!!", null);
			}else {
				logger.info("Rabbit not exist ...");
				try {
					if(rabbitDTO.getStatus() == null) {
						rabbitDTO.setStatus(true);
					}
					logger.info("Rabbit add successfully");
					return new ApiResponse<>(true, "Rabbit added successfully", rabbitService.addRabbit(rabbitDTO));
				} catch (Exception e) {
					return new ApiResponse<>(false, "Failed to add rabbit", null);
				}
			}
		}else {
			logger.info("Rabbit can't saving");
			return new ApiResponse<>(false, "Unable to add a rabbit, insufficient space", null);
		}
	}
	
	@GetMapping("/{id}")
	public ApiResponse<RabbitDTO> getRabbit(@PathVariable String id){
	    logger.info("Request received to get rabbit with ID: {}", id);
	    try {
	        RabbitDTO rabbitDTO = rabbitService.getById(id);
	        logger.info("Rabbit found successfully: {}", rabbitDTO);
	        return new ApiResponse<>(true, "Rabbit found successfully", rabbitDTO);
	    } catch (Exception ex) {
	        logger.error("Error while retrieving rabbit with ID: {}", id, ex);
	        return new ApiResponse<>(false, "Failed to get rabbit", null);
	    }
	}
	
	@PutMapping("/update/{id}")
    public ApiResponse<RabbitDTO> putRabbit(@PathVariable String id, @RequestBody RabbitDTO rabbit) {
        logger.info("Request received to update rabbit with ID: {}", id);
        try {
            RabbitDTO rabbitDTO = rabbitService.getById(id);
            rabbitDTO.setName(rabbit.getName());
            logger.info("Rabbit update successfully with ID: {}", id);
            return new ApiResponse<>(true, "Rabbit update successfully", rabbitService.updateRabbit(rabbitDTO));
        } catch (Exception ex) {
            logger.error("Error while updating rabbit with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to update rabbit", null);
        }
    }
	
	@PutMapping("/soft-delete/{id}")
    public ApiResponse<RabbitDTO> softDeleteRabbit(@PathVariable String id) {
        logger.info("Request soft deleted rabbit with ID: {}", id);
        try {
            RabbitDTO rabbitDTO = rabbitService.getById(id);
            rabbitDTO.setStatus(false);
            logger.info("Rabbit soft delete successfully with ID: {}", id);
            return new ApiResponse<>(true, "Rabbit soft delete successfully", rabbitService.softDelete(rabbitDTO));
        } catch (Exception ex) {
            logger.error("Error while soft deleting rabbit with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to soft delete rabbit", null);
        }
    }
	
	
	@DeleteMapping("/delete/{id}")
	public ApiResponse<RabbitDTO> deleteRabbit(@PathVariable String id){
		logger.info("Request delete rabbit with ID: {}", id);
        try {
            rabbitService.deleteRabbit(id);
            return new ApiResponse<>(true, "Rabbit delete successfully", null);
        } catch (Exception ex) {
            logger.error("Error while soft deleting rabbit with ID: {}", id, ex);
            return new ApiResponse<>(false, "Failed to soft delete rabbit", null);
        }
	}
	

}
