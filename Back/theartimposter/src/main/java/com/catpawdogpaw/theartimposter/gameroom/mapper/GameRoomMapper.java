package com.catpawdogpaw.theartimposter.gameroom.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;

@Mapper
public interface GameRoomMapper {

    void createRoom(GameRoom gameRoom);

    void deleteRoom(@Param("roomId") Long roomId);

    GameRoom getGameRoomById(@Param("roomId") Long roomId);

	List<GameRoom> findAll();


}

