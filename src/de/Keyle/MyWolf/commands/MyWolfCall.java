/*
* Copyright (C) 2011 Keyle
*
* This file is part of MyWolf.
*
* MyWolf is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* MyWolf is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with MyWolf. If not, see <http://www.gnu.org/licenses/>.
*/

package de.Keyle.MyWolf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.Keyle.MyWolf.ConfigBuffer;

public class MyWolfCall implements CommandExecutor
{

    private ConfigBuffer cb;

	public MyWolfCall(ConfigBuffer cb)
	{
	  this.cb = cb;
	}

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
    		Player player = (Player)sender;
    		if(cb.mWolves.containsKey(player.getName()))
    		{
		    	if(cb.Permissions.has(player, "mywolf.call") == false)
				{
					return false;
				}
				cb.mWolves.get(player.getName()).Location = player.getLocation();
				if(cb.mWolves.get(player.getName()).isThere == true && cb.mWolves.get(player.getName()).isDead == false)
				{
					cb.mWolves.get(player.getName()).MyWolf.teleport(player);
					cb.mWolves.get(player.getName()).Location = player.getLocation();
					player.sendMessage(ChatColor.AQUA+cb.mWolves.get(player.getName()).Name + ChatColor.WHITE + " comes to you.");
				}
				else if(cb.mWolves.get(player.getName()).isThere == false && cb.mWolves.get(player.getName()).RespawnTime == 0)
				{
					cb.mWolves.get(player.getName()).Location = player.getLocation();
					cb.mWolves.get(player.getName()).createWolf(false);
					player.sendMessage(ChatColor.AQUA+cb.mWolves.get(player.getName()).Name + ChatColor.WHITE + " comes to you.");
				}
				else if(cb.mWolves.get(player.getName()).isDead == true)
				{
					player.sendMessage(ChatColor.AQUA+cb.mWolves.get(player.getName()).Name + ChatColor.WHITE + " is dead! and respawns in "+ChatColor.GOLD+cb.mWolves.get(player.getName()).RespawnTime+ChatColor.WHITE +" sec");
					return false;
				}
	        }
			else
			{
				sender.sendMessage("You don't have a wolf!");
			}
        }
		return true;
    }
}
