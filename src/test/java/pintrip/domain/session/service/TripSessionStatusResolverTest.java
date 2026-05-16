package pintrip.domain.session.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.session.entity.TripSession;
import pintrip.domain.session.repository.TripSessionQuestReviewRepository;
import pintrip.domain.session.repository.TripSessionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripSessionStatusResolverTest {

    @Mock
    private TripSessionRepository tripSessionRepository;
    @Mock
    private TripSessionQuestReviewRepository reviewRepository;

    @InjectMocks
    private TripSessionStatusResolver resolver;

    @Test
    void resolve_completesWhenSessionExpired() {
        TripSession session = activeSession("session-1");
        setField(session, "expiredAt", LocalDateTime.now().minusMinutes(1));

        when(reviewRepository.countBySession_Id("session-1")).thenReturn(1L);
        when(tripSessionRepository.save(session)).thenReturn(session);

        TripSession resolved = resolver.resolve(session);

        assertThat(resolved.getStatus()).isEqualTo("COMPLETED");
        verify(tripSessionRepository).save(session);
    }

    @Test
    void resolve_completesWhenThreeQuestReviewsExist() {
        TripSession session = activeSession("session-1");

        when(reviewRepository.countBySession_Id("session-1")).thenReturn(3L);
        when(tripSessionRepository.save(session)).thenReturn(session);

        TripSession resolved = resolver.resolve(session);

        assertThat(resolved.getStatus()).isEqualTo("COMPLETED");
        verify(tripSessionRepository).save(session);
    }

    @Test
    void resolve_keepsActiveWhenNotExpiredAndReviewsBelowThree() {
        TripSession session = activeSession("session-1");

        when(reviewRepository.countBySession_Id("session-1")).thenReturn(2L);

        TripSession resolved = resolver.resolve(session);

        assertThat(resolved.getStatus()).isEqualTo("ACTIVE");
    }

    private TripSession activeSession(String id) {
        Dong dong = new Dong();
        TripSession session = TripSession.create(dong);
        setField(session, "id", id);
        return session;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
