package com.catpawdogpaw.theartimposter.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.catpawdogpaw.theartimposter.player.mapper.PlayerMapper;
import com.catpawdogpaw.theartimposter.player.model.Player;

public class PlayerServiceTest {

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePlayer() {
        Player player = new Player();
        player.setId(1L);

        playerService.savePlayer(player);

        verify(playerMapper, times(1)).savePlayer(player);
    }

    @Test
    public void testGetPlayerById() {
        Player player = new Player();
        player.setId(1L);
        when(playerMapper.getPlayerById(1L)).thenReturn(player);

        Player result = playerService.getPlayerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(playerMapper, times(1)).getPlayerById(1L);
    }

    @Test
    public void testGetAllPlayer() {
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);

        List<Player> players = Arrays.asList(player1, player2);
        when(playerMapper.getAllPlayer()).thenReturn(players);

        List<Player> result = playerService.getAllPlayer();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(playerMapper, times(1)).getAllPlayer();
    }
}
