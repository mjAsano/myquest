package com.mjasano.myquestplugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyQuestPlugin extends JavaPlugin implements Listener {

    private List<Quest> quests;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        quests = new ArrayList<>();
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        loadQuests();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveQuests(); // <-- call saveQuests here
    }

    private void loadQuests() {
        //String json = "[{\"id\":1,\"name\":\"Kill the dragon\",\"description\":\"Slay the dragon that has been terrorizing the town.\",\"rewards\":\"Gold, experience points\",\"requirements\":\"None\"},{\"id\":2,\"name\":\"Rescue the princess\",\"description\":\"Rescue the princess who has been kidnapped by the evil sorcerer.\",\"rewards\":\"Magic sword, potion of health\",\"requirements\":\"Magic shield, enchanted armor\"}]";
        //Gson gson = new Gson();
        //quests = gson.fromJson(json, new TypeToken<List<Quest>>() {
        //}.getType());

        // Read the JSON data from the quests.json file in the plugin's data folder
        try (BufferedReader reader = new BufferedReader(new FileReader("quests.json"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            Gson gson = new Gson();
            quests = gson.fromJson(json, new TypeToken<List<Quest>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveQuests() {
        try (FileWriter writer = new FileWriter("quests.json")) {
            Gson gson = new Gson();
            String json = gson.toJson(quests);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createQuest(Player player, String name, String description, String rewards, String requirements) {
        // Get the list of available rewards from the configuration data
        List<String> availableRewards = config.getStringList("rewards");

        // Check if the specified rewards are included in the list of available rewards
        if (availableRewards.containsAll(Arrays.asList(rewards.split(",")))) {
            Quest quest = new Quest(quests.size() + 1, name, description, rewards, requirements);
            quests.add(quest);
            player.sendMessage("Quest created successfully!");
        } else {
            player.sendMessage("Invalid rewards specified. Please choose from the following: " + String.join(", ", availableRewards));
        }
    }

    public void removeQuest(Player player, int questId) {
        for (Quest quest : quests) {
            if (quest.getId() == questId) {
                quests.remove(quest);
                player.sendMessage("Quest removed successfully!");
                return;
            }
        }
        player.sendMessage("Invalid quest id specified. Please use the /quest list command to see a list of available quests.");
    }
    public void listQuests(Player player) {
        if (quests.size() > 0) {
            player.sendMessage("Available quests:");
            for (Quest quest : quests) {
                player.sendMessage(quest.getName() + " - " + quest.getDescription());
            }
        } else {
            player.sendMessage("There are no available quests.");
        }
    }

    public void openQuestListGUI(Player player) {
        // Create a new inventory with the size of a multiple of 9 (e.g. 9, 18, 27, 36, etc.)
        Inventory inventory = Bukkit.createInventory(null, 9, "Quests");

        // Loop through the list of quests and add them to the inventory as items
        for (Quest quest : quests) {
            ItemStack item = new ItemStack(Material.PAPER); // You can use any material for the items in the inventory
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(quest.getName()); // Set the name of the item to the name of the quest
            List<String> lore = new ArrayList<>();
            lore.add(quest.getDescription()); // Set the description of the quest as the lore of the item
            lore.add("Rewards: " + quest.getRewards());
            lore.add("Requirements: " + quest.getRequirements());
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        // Open the inventory for the player
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals("Quests")) {
            event.setCancelled(true); // Cancel the event to prevent the player from taking the item

            ItemStack item = event.getCurrentItem();
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    String questName = meta.getDisplayName();
                    Player player = (Player) event.getWhoClicked();

                    // Check if the player has already confirmed this quest
                    if (player.hasMetadata("quests." + questName)) {
                        // Check if the player right-clicked the item
                        if (event.getClick() == ClickType.RIGHT) {
                            // Remove the quest from the player's metadata
                            player.removeMetadata("quests." + questName, this);

                            // Send a message to the player to confirm that the quest has been cancelled
                            player.sendMessage("Quest cancelled: " + questName);

                            // Refresh the quest list GUI
                            updateInventory(player);
                        } else {
                            player.sendMessage("You have already confirmed this quest!");
                        }
                    } else {
                        // Check if the player left-clicked the item
                        if (event.getClick() == ClickType.LEFT) {
                            // Add the quest to the player's configuration data
                            player.setMetadata("quests." + questName, new FixedMetadataValue(this, true));

                            // Send a message to the player to confirm that the quest has been added
                            player.sendMessage("Quest confirmed: " + questName);

                            // Refresh the quest list GUI
                            updateInventory(player);
                        }
                    }
                }
            }
        }
    }


    private void updateInventory(Player player) {
        // Get the player's inventory and clear it
        Inventory inventory = player.getInventory();
        inventory.clear();

        // Iterate over all the quests the player has confirmed
        for (MetadataValue value : player.getMetadata("quests")) {
            String questName = value.asString();

            // Create a new item with the quest name as the display name and a gold ingot as the icon
            ItemStack item = new ItemStack(Material.GOLD_INGOT);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(questName);
            item.setItemMeta(meta);

            // Add the item to the player's inventory
            inventory.addItem(item);
        }

        // Update the player's inventory
        player.updateInventory();
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("quest")) {
            if (args.length > 0) {
                String subcommand = args[0];
                switch (subcommand) {
                    case "list":
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            listQuests(player);
                        }
                        break;
                    case "create":
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (args.length >= 4) {
                                String name = args[1];
                                String description = args[2];
                                String rewards = args[3];
                                String requirements = args[4];
                                createQuest(player, name, description, rewards, requirements);
                            } else {
                                player.sendMessage("Usage: /quest create <name> <description> <rewards> <requirements>");
                            }
                        }
                        break;
                    case "remove":
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (args.length == 2) {
                                try {
                                    int questId = Integer.parseInt(args[1]);
                                    removeQuest(player, questId);
                                } catch (NumberFormatException e) {
                                    player.sendMessage("Invalid quest id specified. Please specify a valid quest id.");
                                }
                            } else {
                                player.sendMessage("Usage: /quest remove <id>");
                            }
                        }
                        break;
                    case "gui":
                        if (sender instanceof Player) {
                            openQuestListGUI((Player) sender);
                            return true;
                        } else {
                            sender.sendMessage("This command can only be executed by a player!");
                            return false;
                        }
                }
            } else {
                sender.sendMessage("Usage: /quest <list|create|remove|gui>");
            }
            return true;
        }
        return false;
    }
}