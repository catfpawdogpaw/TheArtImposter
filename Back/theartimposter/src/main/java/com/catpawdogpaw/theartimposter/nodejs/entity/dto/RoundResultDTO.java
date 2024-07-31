package com.catpawdogpaw.theartimposter.nodejs.entity.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RoundResultDTO {

	private List<PlayerSTNDTO> players;	
	private String subject;
	private int roundNumber;
	private String winRole;
	private String image;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
