package com.catpawdogpaw.theartimposter.gamesetting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.gamesetting.mapper.GameSettingMapper;
import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;

@Service
public class GameSettingService {

    private final GameSettingMapper gameSettingMapper;

    @Autowired
    public GameSettingService(GameSettingMapper gameSettingMapper) {
        this.gameSettingMapper = gameSettingMapper;
    }

    public void createGameSetting(GameSetting gameSetting) {
        gameSettingMapper.createGameSetting(gameSetting);
    }

    public void deleteGameSetting(Long gameSettingId) {
        gameSettingMapper.deleteGameSetting(gameSettingId);
    }

    public void updateGameSetting(Long gameSettingId, GameSetting gameSetting) {
        gameSetting.setGame_setting_id(gameSettingId);
        gameSettingMapper.updateGameSetting(gameSetting);
    }

    public GameSetting getGameSettingById(Long gameSettingId) {
        return gameSettingMapper.getGameSettingById(gameSettingId);
    }

    public List<GameSetting> findAllGameSettings() {
        return gameSettingMapper.findAllGameSettings();
    }
}