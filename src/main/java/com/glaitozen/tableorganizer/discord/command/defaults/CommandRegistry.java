package com.glaitozen.tableorganizer.discord.command.defaults;

import com.glaitozen.tableorganizer.discord.service.DiscordService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class CommandRegistry {

  private final DiscordService botService;

  public CommandRegistry(DiscordService botService) {
    this.botService = botService;
  }

  public void registerByExecutors(CommandExecutor... commandExecutors) {
    for (CommandExecutor commandExecutor : commandExecutors) {
      Method[] methods = commandExecutor.getClass().getMethods();

      for (Method method : methods) {
        if (method.isAnnotationPresent(CommandInfo.class)) {
          CommandInfo commandInfo = method.getAnnotation(CommandInfo.class);

          Command command = new CommandBuilder()
              .withName(commandInfo.value())
              .withUsage(commandInfo.usage())
              .withMinArguments(commandInfo.minArguments())
              .withMaxArguments(commandInfo.maxArguments())
              .withCommandExecutor(commandExecutor)
              .build();

          this.botService.registerCommand(command);
        }
      }
    }
  }

}
