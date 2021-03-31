package com.Theeef.me.mobs.ai;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.EntityZombie;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import org.bukkit.entity.EntityType;

public class PathfinderGoalMoveTowardsLight extends PathfinderGoal {

    public PathfinderGoalMoveTowardsLight() {
        Class<? extends Entity> entity = EntityZombie.class;
    }

    @Override
    public boolean a() {
        return false;
    }
}
