package com.dcgames.pillars.game.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum GameType {
    NORMAL("Normal Mode","Base game.",0),
    SHUFFLE("Shuffle Mode","You're given a whole random hotbar of items which changes randomly.",0),
    SWAPPER("Swapper Mode","At random intervals, everyone's positions are swapped.",0);

    private final String name;
    private final String descreption;
    @Setter
    private int votes;
}
