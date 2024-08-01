package com.catpawdogpaw.theartimposter.round;

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

import com.catpawdogpaw.theartimposter.round.mapper.RoundMapper;
import com.catpawdogpaw.theartimposter.round.model.Round;

public class RoundServiceTest {

    @Mock
    private RoundMapper roundMapper;

    @InjectMocks
    private RoundService roundService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRoundById() {
        Round round = new Round();
        round.setId(1L);
        when(roundMapper.getRoundById(1L)).thenReturn(round);

        Round result = roundService.getRoundById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(roundMapper, times(1)).getRoundById(1L);
    }

    @Test
    public void testGetAllRounds() {
        Round round1 = new Round();
        round1.setId(1L);
        Round round2 = new Round();
        round2.setId(2L);

        List<Round> rounds = Arrays.asList(round1, round2);
        when(roundMapper.getAllRound()).thenReturn(rounds);

        List<Round> result = roundService.getAllRounds();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roundMapper, times(1)).getAllRound();
    }

    @Test
    public void testInsertRound() {
        Round round = new Round();
        round.setId(1L);

        roundService.insertRound(round);

        verify(roundMapper, times(1)).insertRound(round);
    }

    @Test
    public void testDeleteRound() {
        Long roundId = 1L;

        roundService.deleteRound(roundId);

        verify(roundMapper, times(1)).deleteRound(roundId);
    }

    @Test
    public void testUpdateRound() {
        Round round = new Round();
        round.setId(1L);

        roundService.updateRound(round);

        verify(roundMapper, times(1)).updateRound(round);
    }
}
