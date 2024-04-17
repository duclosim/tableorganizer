package com.glaitozen.tableorganizer;

import com.glaitozen.tableorganizer.discord.command.EchoCommand;
import com.glaitozen.tableorganizer.discord.command.EightBallCommand;
import com.glaitozen.tableorganizer.discord.command.FactCommand;
import com.glaitozen.tableorganizer.discord.command.defaults.CommandRegistry;
import com.glaitozen.tableorganizer.discord.service.DiscordService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TableorganizerApplication implements CommandLineRunner {

	private final DiscordService botService;
	private final CommandRegistry commandRegistry;

    public TableorganizerApplication(DiscordService botService, CommandRegistry commandRegistry) {
        this.botService = botService;
        this.commandRegistry = commandRegistry;
    }

    public static void main(String[] args) {
		SpringApplication.run(TableorganizerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.botService.startBot();

		this.commandRegistry.registerByExecutors(
				new FactCommand(),
				new EightBallCommand(),
				new EchoCommand()
		);
	}
}
