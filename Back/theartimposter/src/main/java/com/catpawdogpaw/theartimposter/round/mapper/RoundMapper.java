package com.catpawdogpaw.theartimposter.round.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.catpawdogpaw.theartimposter.round.model.Round;

@Mapper
public interface RoundMapper {

	Round getRoundById(@Param("roundId") Long roundId);
	List<Round> getAllRound();
	
	void updateRound(Round round);
	
	void insertRound(Round round);
	
	void deleteRound(@Param("roundId") Long roundId);
	
}
