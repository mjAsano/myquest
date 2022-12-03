package com.mjasano.myquestplugin;

public class Quest {

    private int id;
    private String name;
    private String description;
    private String rewards;
    private String requirements;

    public Quest(int id, String name, String description, String rewards, String requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewards = rewards;
        this.requirements = requirements;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRewards() {
        return rewards;
    }

    public String getRequirements() {
        return requirements;
    }

}
