
package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaperRespDTO extends BaseDto{
	
	private String title;
	
	private String publishDate;
	
	private int visits;	
	
	private String description;
	
	private boolean status;
	
	private byte[] img;
	
}
