package com.catpawdogpaw.theartimposter.round.model;

import java.time.LocalDateTime;

import com.catpawdogpaw.theartimposter.player.model.GameRole;

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
