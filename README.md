# myquest
Minecraft is a popular sandbox video game that allows players to build and explore virtual worlds made up of blocks. One of the features of the game is the ability to create and manage custom quests, which can add an extra layer of challenge and excitement for players.

To support this functionality, a plugin called "MyQuestPlugin" has been developed in the Java programming language using the Bukkit API. This plugin allows players to create and manage quests within the game, and it saves the created quests to a file so that they can be loaded again in the future.

The plugin implements the JavaPlugin class and the Listener interface, which enables it to handle events in the game. When the plugin is loaded, it reads in a list of available rewards from the game's configuration data, and it uses this information to validate the rewards specified for each quest.

Once a quest is created, players can see a list of available quests and choose which ones to complete. Upon completion of a quest, players receive the specified rewards, which may include items such as gold, experience points, or special items like magic swords or potions.

In addition to creating and managing quests, the plugin also provides a way for players to view the details of a quest, including its requirements and rewards. This allows players to plan and prepare for a quest before attempting to complete it.

Overall, the MyQuestPlugin adds a fun and engaging aspect to the game of Minecraft, and it offers players an additional way to challenge themselves and explore the virtual world.

## Doc 
### Main Class

`MyQuestPlugin` class is a plugin for the game "Minecraft" that allows players to create and manage custom quests. It extends the JavaPlugin class and implements the Listener interface to handle events in the game.

`onEnable` and onDisable methods are called when the plugin is loaded and unloaded, respectively. The onEnable method initializes the quests list and loads the quests from a file, while the onDisable method saves the quests to the file.

`loadQuests` method reads in the JSON data from the quests file and uses the Gson library to deserialize the data into a list of Quest objects. The saveQuests method does the opposite, serializing the quests list and writing it to the quests file.

`createQuest` method allows players to create a new quest with the specified name, description, rewards, and requirements. It first checks if the specified rewards are included in the list of available rewards from the game's configuration data. If they are, it creates a new Quest object and adds it to the quests list.

`getQuest` method searches the quests list for a quest with the specified ID, and it returns the quest if found. The getQuests method simply returns the quests list.

`updateQuest` method allows players to update the details of an existing quest. It first gets the quest with the specified ID, and then it updates the quest's name, description, rewards, and requirements with the provided values. Finally, it saves the updated quest to the file.

`deleteQuest` method allows players to delete a quest from the quests list. It first gets the quest with the specified ID, and then it removes it from the list and saves the updated list to the file.

`onInventoryClick` method is called when a player clicks on an item in an inventory. It is used to handle the actions for the quest creation and management menu. It gets the metadata for the clicked item and performs the appropriate action based on the metadata value.

### Quest Class
```java
package com.mjasano.myquestplugin;

/**
 * The `Quest` class represents a quest in a game.
 * A quest has an ID, a name, a description, rewards, and requirements.
 */
public class Quest {

    /**
     * The ID of the quest.
     */
    private int id;

    /**
     * The name of the quest.
     */
    private String name;

    /**
     * The description of the quest.
     */
    private String description;

    /**
     * The rewards for completing the quest.
     */
    private String rewards;

    /**
     * The requirements for completing the quest.
     */
    private String requirements;

    /**
     * Constructs a new `Quest` object with the given ID, name, description, rewards, and requirements.
     *
     * @param id The ID of the quest.
     * @param name The name of the quest.
     * @param description The description of the quest.
     * @param rewards The rewards for completing the quest.
     * @param requirements The requirements for completing the quest.
     */
    public Quest(int id, String name, String description, String rewards, String requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewards = rewards;
        this.requirements = requirements;
    }

    /**
     * Returns the ID of the quest.
     *
     * @return The ID of the quest.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the quest.
     *
     * @return The name of the quest.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the quest.
     *
     * @return The description of the quest.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the rewards for completing the quest.
     *
     * @return The rewards for completing the quest.
     */
    public String getRewards() {
        return rewards;
    }

    /**
     * Returns the requirements for completing the quest.
     *
     * @return The requirements for completing the quest.
     */
    public String getRequirements() {
        return requirements;
    }

}
```
