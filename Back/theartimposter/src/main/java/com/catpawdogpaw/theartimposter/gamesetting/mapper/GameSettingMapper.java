package com.catpawdogpaw.theartimposter.gamesetting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;

@Mapper
public interface GameSettingMapper {
	void createGameSetting(GameSetting gameSetting);

	void deleteGameSetting(@Param("id") Long id);

	 void updateGameSetting(GameSetting gameSetting);
	 
	GameSetting getGameSettingById(@Param("id") Long id);

	List<GameSetting> findAllGameSettings();
}
