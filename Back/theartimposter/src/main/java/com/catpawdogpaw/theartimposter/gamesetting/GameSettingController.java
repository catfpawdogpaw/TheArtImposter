package com.catpawdogpaw.theartimposter.gamesetting;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gamesetting")
public class GameSettingController {

    private final GameSettingService gameSettingService;

    @PostMapping("/create")
    public void createGameSetting(@RequestBody GameSetting gameSetting) {
        gameSettingService.createGameSetting(gameSetting);
    }

//    @DeleteMapping("/delete/{id}")
//    public void deleteGameSetting(@PathVariable("id") Long id) {
//        gameSettingService.deleteGameSetting(id);
//    }

    @PatchMapping("/update/{id}")
    public void updateGameSetting(@PathVariable("id") Long id, @RequestBody GameSetting gameSetting) {
        gameSettingService.updateGameSetting(id, gameSetting);
    }

    @GetMapping("/{id}")
    public GameSetting getGameSettingById(@PathVariable("id") Long id) {
        return gameSettingService.getGameSettingById(id);
    }

    @GetMapping
    public List<GameSetting> findAllGameSettings() {
        return gameSettingService.findAllGameSettings();
    }
}
