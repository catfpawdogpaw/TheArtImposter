package com.catpawdogpaw.theartimposter.round;


import java.util.List;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.nodejs.entity.dto.RoundResultDTO;
import com.catpawdogpaw.theartimposter.player.model.GameRole;
import com.catpawdogpaw.theartimposter.round.mapper.RoundMapper;
import com.catpawdogpaw.theartimposter.round.model.Round;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoundService {
	private final RoundMapper roundMapper;
	 public Round getRoundById(Long roundId) {
	        return roundMapper.getRoundById(roundId);
	    }

	    public List<Round> getAllRounds() {
	        return roundMapper.getAllRound();
	    }

	    public void insertRound(Round round) {
	        roundMapper.insertRound(round);
	    }

	    public void deleteRound(Long roundId) {
	        roundMapper.deleteRound(roundId);
	    }

	    public void updateRound(Round round) {
	        roundMapper.updateRound(round);
	    }

		public void saveRoundResult(RoundResultDTO roundResultDTO) {
			  Round round = new Round();
		        round.setRoundNum(roundResultDTO.getRoundNumber());
		        round.setWinRole(GameRole.valueOf(roundResultDTO.getWinRole().toUpperCase())); // assuming GameRole is an enum
		        round.setImageUrl(roundResultDTO.getImage());
		        round.setStartedAt(roundResultDTO.getStartTime());
		        round.setFinishedAt(roundResultDTO.getEndTime());
		        roundMapper.insertRound(round);
			
		}

		
	
}
