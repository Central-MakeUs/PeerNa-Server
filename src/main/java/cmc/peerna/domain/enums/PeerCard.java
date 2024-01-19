package cmc.peerna.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PeerCard {

    // Group 1
    DRIVING("추진하는"),
    COOPERATIVE("협조하는"),

    // Group 2
    ANALYTICAL("분석적인"),
    COMPREHENSIVE("종합적인"),
    FUTURE_ORIENTED("미래지향적인"),

    // Group 3
    PRAGMATIC("냉철한"),
    MULTIDIMENSIONAL("입체적인"),
    WARMHEARTED("따뜻한"),

    // Group 4
    CAUTIOUS("신중한"),
    CHALLENGING("도전적인");

    private final String description;
}
