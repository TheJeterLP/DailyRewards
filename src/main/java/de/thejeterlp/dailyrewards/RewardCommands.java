/*
 * Copyright (C) 2019 TheJeterLP
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package de.thejeterlp.dailyrewards;

import de.thejeterlp.dailyrewards.commands.BaseCommand;
import de.thejeterlp.dailyrewards.commands.BaseCommand.Sender;
import de.thejeterlp.dailyrewards.commands.CommandArgs;
import de.thejeterlp.dailyrewards.commands.CommandArgs.Flag;
import de.thejeterlp.dailyrewards.commands.CommandHandler;
import de.thejeterlp.dailyrewards.commands.CommandResult;
import de.thejeterlp.dailyrewards.commands.HelpPage;
import de.thejeterlp.dailyrewards.player.PlayerManager;
import de.thejeterlp.dailyrewards.player.SQLPlayer;
import de.thejeterlp.dailyrewards.utils.ItemSettings;
import de.thejeterlp.dailyrewards.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@CommandHandler
public class RewardCommands {

    private final HelpPage helpPage = new HelpPage("rewards");

    public RewardCommands() {
        helpPage.addPage("", "Shows the plugin infos");
        helpPage.addPage("set <day>", "Sets the reward for the given day as your inventory.");
        helpPage.addPage("grab", "Grabs the reward for today");
        helpPage.addPage("give -p <playername> -d <day>", "Gives the given player a reward manually");
        helpPage.prepare();
    }

    @BaseCommand(command = "rewards", sender = Sender.PLAYER)
    public CommandResult executeRewards(CommandSender sender, CommandArgs args) {
        if (helpPage.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }
        if (!args.isEmpty()) {
            return CommandResult.ERROR;
        }
        sender.sendMessage("§aDailyRewards plugin by " + DailyRewards.getInstance().getDescription().getAuthors() + " Version: " + DailyRewards.getInstance().getDescription().getVersion());
        return CommandResult.SUCCESS;
    }

    @BaseCommand(command = "rewards", sender = Sender.CONSOLE)
    public CommandResult executeRewardsConsole(CommandSender sender, CommandArgs args) {
        return executeRewards(sender, args);

    }

    @BaseCommand(command = "rewards", sender = Sender.PLAYER, subCommand = "set", permission = "dailyrewards.set")
    public CommandResult executeSet(Player sender, CommandArgs args) {
        if (args.getLength() != 1) {
            return CommandResult.ERROR;
        }

        if (!args.isInteger(0)) {
            return CommandResult.NOT_A_NUMBER;
        }

        Inventory invsender = sender.getInventory();
        ItemSettings.setReward(args.getInt(0), invsender);

        sender.sendMessage("§aYour Inventory has been copied.");
        return CommandResult.SUCCESS;
    }

    @BaseCommand(command = "rewards", sender = Sender.PLAYER, subCommand = "grab", permission = "dailyrewards.grab")
    public CommandResult executeGrab(Player sender, CommandArgs args) {
        SQLPlayer sqp = PlayerManager.getPlayer(sender);
        if (sqp.hasGrabbedInventory()) {
            sender.sendMessage("§4You already used the grab command today, come back tomorrow!");
            return CommandResult.SUCCESS;
        }

        int day = sqp.getDaysInRow();

        List<ItemStack> stack = ItemSettings.getReward(day);

        List<ItemStack> ns = new ArrayList<>();
        //cleanup
        for (ItemStack s : stack) {
            if (s != null && s.getType() != Material.AIR) {
                ns.add(s);
            }
        }

        int free = Utils.freePlacesInInv(sender);
        if (free < ns.size()) {
            sender.sendMessage("§4You do NOT have enough space in your Inventory!");
            return CommandResult.SUCCESS;
        }

        for (ItemStack s : ns) {
            sender.getInventory().addItem(s);
        }

        sqp.setGrabbed(true);
        sender.sendMessage("§aSuccessfully grabbed todays reward.");
        return CommandResult.SUCCESS;
    }

    @BaseCommand(command = "rewards", sender = Sender.PLAYER, subCommand = "give", permission = "dailyrewards.give")
    public CommandResult executeGive(Player sender, CommandArgs args) {
        if (!args.hasFlag("p") || !args.hasFlag("d")) {
            return CommandResult.ERROR;
        }

        Flag fp = args.getFlag("p");
        Flag d = args.getFlag("d");

        if (!d.isInteger()) {
            return CommandResult.NOT_A_NUMBER;
        }
        if (!fp.isPlayer()) {
            return CommandResult.NOT_ONLINE;
        }

        int day = d.getInt();

        List<ItemStack> stack = ItemSettings.getReward(day);

        List<ItemStack> ns = new ArrayList<>();
        //cleanup
        for (ItemStack s : stack) {
            if (s != null && s.getType() != Material.AIR) {
                ns.add(s);
            }
        }

        int free = Utils.freePlacesInInv(fp.getPlayer());
        if (free < ns.size()) {
            sender.sendMessage("§4The Player does NOT have enough space in his Inventory!");
            return CommandResult.SUCCESS;
        }

        for (ItemStack s : ns) {
            fp.getPlayer().getInventory().addItem(s);
        }

        sender.sendMessage("§aSuccessfully gave the reward for the day " + day + " to " + fp.getPlayer().getDisplayName());
        return CommandResult.SUCCESS;
    }

}
