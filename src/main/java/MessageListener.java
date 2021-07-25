import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (!event.getAuthor().isBot()) {

            if (event.getMessage().getContentRaw().replace("!", "").equals(event.getJDA().getSelfUser().getAsMention())) {
                event.getChannel().sendMessage("Hello! I have only one command: `$crashers`. This command will display how many crashers I have deleted. " +
                        "My function is fully automated, so there is no need for configuration. I will automatically delete any crashers in any channel that I " +
                        "have access to, and leave a message behind stating who posted it!").queue();

                if (!event.getGuild().getSelfMember().getPermissions().contains(Permission.MESSAGE_MANAGE)) {
                    event.getChannel().sendMessage("I am missing the `MESSAGE_MANAGE` permission! This permission is critical for the bot to function.").queue();
                }
            }

            if (event.getMessage().getContentRaw().equals("$crashers")) {
                try {
                    event.getChannel().sendMessage("Crashers deleted: " + Integer.parseInt(Files.readAllLines(Paths.get("counter.txt")).get(0)) + "!").queue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            CheckVideo checkVideo = new CheckVideo();

            try {
                checkVideo.isCrasher(event);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}