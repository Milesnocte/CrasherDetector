package Main;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class JDA extends ListenerAdapter implements EventListener {
    public static void main(String[] args) throws Exception {

        //Online the bot and create the listeners
        DefaultShardManagerBuilder.create(
                GatewayIntent.GUILD_MESSAGES
        ).setToken(Credentials.TOKEN)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .addEventListeners(
                    new MessageListener(),
                    new BotEvents()
                )
                .setStatus(OnlineStatus.ONLINE)
                .build();

        //Print invite link to console
        System.out.println("https://discord.com/api/oauth2/authorize?client_id=" + "844845429660057620" + "&permissions=8&scope=bot");

    }
}
