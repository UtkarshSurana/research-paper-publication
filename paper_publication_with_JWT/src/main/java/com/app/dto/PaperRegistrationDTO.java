package com.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class PaperRegistrationDTO extends BaseDto {
	
	@NotBlank(message = "Paper name is required")
	private String title;
	@NotNull(message = "Publish date must be supplied")
	private String publishDate;
	@NotBlank(message = "contents must be supplied")
	private String description;	
	
	private MultipartFile imgFile;
	
	private MultipartFile pdfFile;
	
}
