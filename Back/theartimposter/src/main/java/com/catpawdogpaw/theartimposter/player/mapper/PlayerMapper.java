package com.catpawdogpaw.theartimposter.player.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlayerMapper {
	public void savePlayer();
	public void updatePlaye(Long id);

}
