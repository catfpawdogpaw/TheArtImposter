package com.catpawdogpaw.theartimposter.gameroom.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;

@Mapper
public interface GameRoomMapper {

    void createGameRoom(GameRoom gameRoom);

    void deleteGameRoom(@Param("gameRoomId") Long gameRoomId);

    GameRoom getGameRoomById(@Param("gameRoomId") Long gameRoomId);

	List<GameRoom> findAll();


}

