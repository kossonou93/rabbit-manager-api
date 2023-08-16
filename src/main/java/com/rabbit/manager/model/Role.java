package com.rabbit.manager.model;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document("role")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AuditModel{
	
	@Id
	private String id;
	private String name;

}
