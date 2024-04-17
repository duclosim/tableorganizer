package com.glaitozen.tableorganizer.discord.command.defaults;

public class Command {

  private String name;
  private String usage;
  private int minArguments;
  private int maxArguments;
  private CommandExecutor executor;

  Command(CommandBuilder commandBuilder) {
    this.name = commandBuilder.getName();
    this.usage = commandBuilder.getUsage();
    this.minArguments = commandBuilder.getMinArguments();
    this.maxArguments = commandBuilder.getMaxArguments();
    this.executor = commandBuilder.getExecutor();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUsage(String usage) {
    this.usage = usage;
  }

  public void setMinArguments(int minArguments) {
    this.minArguments = minArguments;
  }

  public void setMaxArguments(int maxArguments) {
    this.maxArguments = maxArguments;
  }

  public void setExecutor(CommandExecutor executor) {
    this.executor = executor;
  }

  public String getName() {
    return this.name;
  }

  public String getUsage() {
    return this.usage;
  }

  public int getMinArguments() {
    return this.minArguments;
  }

  public int getMaxArguments() {
    return this.maxArguments;
  }

  public CommandExecutor getExecutor() {
    return this.executor;
  }
}
