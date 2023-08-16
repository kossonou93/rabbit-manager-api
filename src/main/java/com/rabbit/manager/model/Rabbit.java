package com.rabbit.manager.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Document("rabbit")
public class Rabbit extends AuditModel{
	
	private String id;
	private String name;
	private Boolean status;
	private String imagePath;
}
