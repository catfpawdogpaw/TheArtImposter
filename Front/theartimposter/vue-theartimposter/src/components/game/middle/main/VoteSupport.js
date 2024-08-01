import { EventBus } from '@/utils/eventBus';

export default {
    showVoteResultModal() {
        // EventBus를 통해 VoteResultModal을 열기
        EventBus.$emit('voteResult', {
            voteResults: [
                { username: 'Alice', votes: 3, role: 'artist' },
                { username: 'Bob', votes: 5, role: 'fake_artist' },
            ],
            abstentionCount: 2,
        });
    },
    showResultFakeArtist() {
        EventBus.$emit('roundEnd', {
            winner: 'fake_artist',
        });
    },
    showResultArtist() {
        EventBus.$emit('roundEnd', {
            winner: 'artist',
        });
    },
    showGuessWaitingModal() {
        EventBus.$emit('fakeArtistGuessStart1');
    },
    showFakeArtistGuessModal() {
        EventBus.$emit('fakeArtistGuessStart');
    },
};
