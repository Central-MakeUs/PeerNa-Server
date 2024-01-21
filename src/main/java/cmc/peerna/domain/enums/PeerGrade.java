package cmc.peerna.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PeerGrade {
    OUTSTANDING(98),
    EXCELLENT(84),
    GOOD(70),
    AVERAGE(56),
    BELOW_AVERAGE(42),
    POOR(28);
    private final int score;
}
