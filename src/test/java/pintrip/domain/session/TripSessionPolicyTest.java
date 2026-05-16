package pintrip.domain.session;

import org.junit.jupiter.api.Test;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.session.entity.TripSession;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TripSessionPolicyTest {

    @Test
    void expireDays_isTwo() {
        assertThat(TripSessionPolicy.EXPIRE_DAYS).isEqualTo(2);
    }

    @Test
    void requiredQuestReviews_isThree() {
        assertThat(TripSessionPolicy.REQUIRED_QUEST_REVIEWS).isEqualTo(3);
    }

    @Test
    void createSession_setsExpiredAtAfterTwoDays() {
        Dong dong = new Dong();
        TripSession session = TripSession.create(dong);

        LocalDateTime expected = session.getCreatedAt().plusDays(2);
        assertThat(session.getExpiredAt()).isEqualTo(expected);
    }
}
