package com.catpawdogpaw.theartimposter.player;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.nodejs.entity.dto.PlayerNTSDTO;
import com.catpawdogpaw.theartimposter.player.mapper.PlayerMapper;
import com.catpawdogpaw.theartimposter.player.model.GameRole;
import com.catpawdogpaw.theartimposter.player.model.Player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerService {
	
	private final PlayerMapper playerMapper;
	
	public void savePlayer(Player player) {
		playerMapper.savePlayer(player);
	}
	
	public Player getPlayerById(Long id) {
		return playerMapper.getPlayerById(id);
	}
	
	public List<Player> getAllPlayer(){
		return playerMapper.getAllPlayer();
	}

	public void savePlayerFromDTO(PlayerNTSDTO playerNTSDTO) {
        Player player = new Player();
        player.setUserId( playerNTSDTO.getUserId());
        player.setTurn(playerNTSDTO.getTurn());
        player.setColor(playerNTSDTO.getColor());
        player.setGameRole(GameRole.valueOf(playerNTSDTO.getGameRole().toUpperCase())); 
        savePlayer(player);
    }
	
	
}
