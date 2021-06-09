import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotEvents extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        updateActivity(event);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        updateActivity(event);
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        updateActivity(event);
    }

    // Update the bot status to display the number of servers its in
    public void updateActivity(Event event){
        int guildCount = event.getJDA().getGuilds().size();
        event.getJDA().getPresence().setActivity(Activity.watching(guildCount + " Guilds"));
    }
}
