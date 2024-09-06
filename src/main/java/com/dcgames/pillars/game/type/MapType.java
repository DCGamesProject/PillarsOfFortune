package com.dcgames.pillars.game.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum MapType {
  NORMAL("Normal Mode","Base game.",0),
  RISING_LAVA("Rising Lava Mode","Lava spawns at the bottom of the map, and slowly rises.",0),
  PORTALS("Portals Mode","At the top and bottom of the map, portals are spawned\nif you fall into a bottom portal, you're teleported to the top.",0),
  ABLOCKALYPSE("Ablockalypse Mode","Random blocks spawn around the map",0),
  FRAGILE_BLOCKS("Fragile Block Mode","Blocks you stand on will slowly break over time, so keep moving!",0);

  private final String name;
  private final String descreption;
  @Setter
  private int votes;

}