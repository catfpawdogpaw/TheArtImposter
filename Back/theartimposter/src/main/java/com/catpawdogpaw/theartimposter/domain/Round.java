package com.catpawdogpaw.theartimposter.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Round {
	private Long id;
	private Long roomId;
	private Long subjectId;
	private int roundNum;
	private GameRole winRole;
	private String imageUrl;
	private LocalDateTime startedAt;
	private LocalDateTime finishedAt;
}
