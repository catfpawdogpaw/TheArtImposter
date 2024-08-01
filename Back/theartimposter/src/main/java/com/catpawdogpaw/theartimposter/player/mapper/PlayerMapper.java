package com.catpawdogpaw.theartimposter.player.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.catpawdogpaw.theartimposter.player.model.Player;

@Mapper
public interface PlayerMapper {
	void savePlayer(Player player);
	void updatePlaye(Long id);
	Player getPlayerById(@Param("playerId") Long playerId);
	List<Player> getAllPlayer();
	

}
