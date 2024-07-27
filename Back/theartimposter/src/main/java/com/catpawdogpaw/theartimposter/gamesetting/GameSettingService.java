package com.catpawdogpaw.theartimposter.gamesetting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.gamesetting.mapper.GameSettingMapper;
import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameSettingService {

    private final GameSettingMapper gameSettingMapper;

    public void createGameSetting(GameSetting gameSetting) {
        gameSettingMapper.createGameSetting(gameSetting);
    }

    public void deleteGameSetting(Long gameSettingId) {
        gameSettingMapper.deleteGameSetting(gameSettingId);
    }

    public void updateGameSetting(Long gameSettingId, GameSetting newGameSetting) {
        // 기존 데이터 로드
        GameSetting existingGameSetting = gameSettingMapper.getGameSettingById(gameSettingId);
        
        if (existingGameSetting != null) {
            // 새로운 값으로 필드 업데이트 (null이 아닌 경우)
            if (newGameSetting.getVersion() != null) {
                existingGameSetting.setVersion(newGameSetting.getVersion());
            }
            if (newGameSetting.getTurnTimeLimit() != 0) {
                existingGameSetting.setTurnTimeLimit(newGameSetting.getTurnTimeLimit());
            }
            if (newGameSetting.getRoundTimeLimit() != 0) {
                existingGameSetting.setRoundTimeLimit(newGameSetting.getRoundTimeLimit());
            }
            if (newGameSetting.getMinPeople() != 0) {
                existingGameSetting.setMinPeople(newGameSetting.getMinPeople());
            }
            if (newGameSetting.getMaxPeople() != 0) {
                existingGameSetting.setMaxPeople(newGameSetting.getMaxPeople());
            }
            if (newGameSetting.getRound() != 0) {
                existingGameSetting.setRound(newGameSetting.getRound());
            }
            
            // 업데이트 실행
            gameSettingMapper.updateGameSetting(existingGameSetting);
        } else {
            throw new RuntimeException("GameSetting not found for id: " + gameSettingId);
        }
    }

    public GameSetting getGameSettingById(Long gameSettingId) {
        return gameSettingMapper.getGameSettingById(gameSettingId);
    }

    public List<GameSetting> findAllGameSettings() {
        return gameSettingMapper.findAllGameSettings();
    }
}