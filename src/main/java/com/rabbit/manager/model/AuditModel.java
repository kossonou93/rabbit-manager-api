package com.rabbit.manager.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public abstract class AuditModel {

	@Column(name="created_at", nullable=false)
	@CreatedDate
	private Date createAt;
	
	@Column(name="updated_at", nullable=false)
	@LastModifiedDate
	private Date updateAt;
	
}
