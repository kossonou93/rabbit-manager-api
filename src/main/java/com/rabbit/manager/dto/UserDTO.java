package com.rabbit.manager.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private String id;
	private String name;
	private String username;
	private String email;
	private String password;
	private Boolean status;
	private List<String> roles;
}
