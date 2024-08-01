package com.catpawdogpaw.theartimposter.nodejs.entity.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoundResultDTO {

	private List<PlayerSTNDTO> players;	
	private String subject;
	private int roundNumber;
	private String winRole;
	private String image; // base64 encoded image string
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
