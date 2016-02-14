/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2016 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.util.hooks;

import de.Keyle.MyPet.api.player.MyPetPlayer;
import de.Keyle.MyPet.util.logger.DebugLogger;
import de.Keyle.MyPet.util.logger.MyPetLogger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyHook {
    public static boolean USE_ECONOMY = true;
    private static boolean searchedVaultEconomy = false;
    private static Economy economy = null;

    public static boolean canUseEconomy() {
        if (!USE_ECONOMY) {
            return false;
        }
        if (!searchedVaultEconomy) {
            setupEconomy();
        }
        return economy != null;
    }

    public static boolean canPay(MyPetPlayer petOwner, double costs) {
        if (!USE_ECONOMY) {
            return true;
        }
        if (!searchedVaultEconomy) {
            setupEconomy();
        }
        if (economy != null && economy.isEnabled()) {
            try {
                return economy.has(Bukkit.getOfflinePlayer(petOwner.getPlayerUUID()), costs);
            } catch (Exception e) {
                e.printStackTrace();
                DebugLogger.printThrowable(e);
                MyPetLogger.write("The economy plugin threw an exception, economy support disabled.");
                USE_ECONOMY = false;
            }
        }
        return true;
    }

    public static boolean pay(MyPetPlayer petOwner, double costs) {
        if (!USE_ECONOMY) {
            return true;
        }
        if (!searchedVaultEconomy) {
            setupEconomy();
        }
        if (economy != null && economy.isEnabled()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(petOwner.getPlayerUUID());
            if (economy.has(player, costs)) {
                try {
                    return economy.withdrawPlayer(player, costs).transactionSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLogger.printThrowable(e);
                    MyPetLogger.write("The economy plugin threw an exception, economy support disabled.");
                    USE_ECONOMY = false;
                }
            }
            return false;
        }
        return true;
    }

    public static void reset() {
        USE_ECONOMY = false;
        searchedVaultEconomy = false;
        economy = null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static void setupEconomy() {
        if (PluginHookManager.isPluginUsable("Vault")) {
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
                searchedVaultEconomy = true;
                DebugLogger.info("Economy hook enabled.");
                return;
            }
        }
        DebugLogger.info("No Economy plugin found. Economy hook not enabled.");
        searchedVaultEconomy = true;
    }
}