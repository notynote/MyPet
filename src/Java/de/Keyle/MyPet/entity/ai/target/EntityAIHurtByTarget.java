/*
 * Copyright (C) 2011-2013 Keyle
 *
 * This file is part of MyPet
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyPet. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.ai.target;

import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.util.MyPetPvP;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityPlayer;
import net.minecraft.server.v1_4_R1.PathfinderGoalHurtByTarget;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityAIHurtByTarget extends PathfinderGoalHurtByTarget
{
    MyPet myPet;

    public EntityAIHurtByTarget(EntityMyPet petEntity, boolean b)
    {
        super(petEntity, b);
        myPet = petEntity.getMyPet();
    }

    @Override
    public boolean a()
    {
        if (d.aC() instanceof EntityPlayer)
        {
            Player targetPlayer = null;
            try
            {
                Method gBE = EntityHuman.class.getDeclaredMethod("getBukkitEntity");
                gBE.setAccessible(true);
                targetPlayer = (Player) gBE.invoke(d.aC());
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e1)
            {
                e1.printStackTrace();
            }
            catch (InvocationTargetException e1)
            {
                e1.printStackTrace();
            }

            if (targetPlayer == myPet.getOwner().getPlayer())
            {
                return false;
            }
            else if (MyPetPvP.canHurt(myPet.getOwner().getPlayer(), targetPlayer))
            {
                return super.a();
            }
            else
            {
                return false;
            }
        }
        else if (d.aC() instanceof EntityMyPet)
        {
            MyPet targetMyPet = ((EntityMyPet) d.aC()).getMyPet();
            if (MyPetPvP.canHurt(myPet.getOwner().getPlayer(), targetMyPet.getOwner().getPlayer()))
            {
                return super.a();
            }
            else
            {
                return false;
            }
        }
        return super.a();
    }
}