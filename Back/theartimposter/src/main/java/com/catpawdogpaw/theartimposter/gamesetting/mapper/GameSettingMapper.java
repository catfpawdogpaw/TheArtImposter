package com.catpawdogpaw.theartimposter.gamesetting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;

@Mapper
public interface GameSettingMapper {
	void createGameSetting(GameSetting gameSetting);

	void deleteGameSetting(@Param("game_setting_id") Long gameSettingId);

	void updateGameSetting(@Param("game_setting_id") GameSetting gameSetting);

	GameSetting getGameSettingById(@Param("game_setting_id") Long gameSettingId);

	List<GameSetting> findAllGameSettings();
}
