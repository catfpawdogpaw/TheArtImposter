package com.catpawdogpaw.theartimposter.gamesetting;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gamesettings")
public class GameSettingController {

    private final GameSettingService gameSettingService;

    @PostMapping
    public void createGameSetting(@RequestBody GameSetting gameSetting) {
        gameSettingService.createGameSetting(gameSetting);
    }

    @DeleteMapping("/{id}")
    public void deleteGameSetting(@PathVariable Long id) {
        gameSettingService.deleteGameSetting(id);
    }

    @PutMapping("/{id}")
    public void updateGameSetting(@PathVariable Long id, @RequestBody GameSetting gameSetting) {
        gameSettingService.updateGameSetting(id, gameSetting);
    }

    @GetMapping("/{id}")
    public GameSetting getGameSettingById(@PathVariable Long id) {
        return gameSettingService.getGameSettingById(id);
    }

    @GetMapping
    public List<GameSetting> findAllGameSettings() {
        return gameSettingService.findAllGameSettings();
    }
}
