import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckVideo {

    private final List<String> formats = Arrays.asList(".gif", ".gifv", ".webm", ".mp4", "mov");
    private List<String> link_url = new ArrayList<>();

    public void isCrasher(GuildMessageReceivedEvent event) throws IOException {
        if(!event.getMessage().getAttachments().isEmpty()){
            String vid_url = event.getMessage().getAttachments().get(0).getUrl();
            checkFrames(FFCheck(vid_url), event);
        }

        // Check the message for links and add each to a list
        if(event.getMessage().getContentRaw().toLowerCase().contains("https://") || event.getMessage().getContentRaw().toLowerCase().contains("http://")){
            String[] message = event.getMessage().getContentRaw().split("\\s+");
            for(String urlCheck : message){
                if(urlCheck.contains("https://") || urlCheck.contains("http://")){
                    link_url.add(urlCheck);
                }
            }

            if(!link_url.isEmpty()){
                for(String url : link_url) {
                    // Gfycat needs to be webscraped to get the actual url, because.. gfycat ..
                    String srsURL;
                    if(url.contains("gfycat.com") && !url.contains("giant.gfycat.com") && !url.contains("giant.gfycat.com")){
                        Document doc = Jsoup.connect(url).get();
                        Elements video = doc.select("video");
                        srsURL = video.html().substring(video.html().indexOf("https://giant."));
                        srsURL = srsURL.substring(0, srsURL.indexOf(".mp4")+4);
                    }else{
                        srsURL = url;
                    }
                    checkFrames(FFCheck(srsURL), event);
                }
            }
        }
    }

    private BufferedReader FFCheck(String url) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process;

        process = runtime.exec("ffprobe -hide_banner -loglevel quiet -select_streams v:0 -print_format " +
                "json -show_frames -skip_frame nokey -show_entries frame=pix_fmt,width,height -i " + url);

        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    private void checkFrames(BufferedReader input, GuildMessageReceivedEvent event) throws IOException {

        FileWriter fileWriter;
        String height = null;
        String width = null;
        String pixFMT = null;
        String line;

        // The output of the above checks will be video height, width, and pixel format. This will check every line of the output
        // to ensure neither of these 3 change at any point, if it does get rid of it.
        while ((line = input.readLine()) != null) {
            if (line.contains("width")) {
                String num = line.replaceAll(",", "").replaceAll("\\s+","");
                if (width == null) {
                    width = num;
                } else {
                    if (!width.equals(num)) {

                        if(!event.getGuild().getSelfMember().getPermissions().contains(Permission.MESSAGE_MANAGE)){
                            event.getChannel().sendMessage("CRASHER FOUND, I am missing the `MESSAGE_MANAGE` permission! This permission is critical for the bot to function.\nMessage Link: " +
                                    "https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessage().getId() + "\nPosted by: "
                                    + event.getAuthor().getAsMention()).queue();
                        }

                        //Delete message and log to channel
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage("Crasher removed! Posted by " + event.getMessage().getAuthor().getAsMention() + "\n" +
                                "Video width changed from `" + width.substring(width.indexOf(":") + 1) + "` to `" + num.substring(num.indexOf(":") + 1) + "`").queue();

                        //Keep track of crashers deleted
                        int crashersDeleted = Integer.parseInt(Files.readAllLines(Paths.get("counter.txt")).get(0));
                        fileWriter = new FileWriter("counter.txt");
                        fileWriter.write(String.valueOf(crashersDeleted + 1));
                        fileWriter.close();
                        break;
                    }
                }
            }
            if (line.contains("height")) {
                String num = line.replaceAll(",", "").replaceAll("\\s+","");
                if (height == null) {
                    height = num;
                } else {
                    if (!height.equals(num)) {

                        if(!event.getGuild().getSelfMember().getPermissions().contains(Permission.MESSAGE_MANAGE)){
                            event.getChannel().sendMessage("CRASHER FOUND, I am missing the `MESSAGE_MANAGE` permission! This permission is critical for the bot to function.\nMessage Link:" +
                                    "https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessage().getId() + "\nPosted by: "
                                    + event.getAuthor().getAsMention()).queue();
                        }

                        //Delete message and log to channel
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage("Crasher removed! Posted by " + event.getMessage().getAuthor().getAsMention() + "\n" +
                                "Video height changed from `" + height.substring(height.indexOf(":")+1) + "` to `" + num.substring(num.indexOf(":") + 2) + "`").queue();

                        //Keep track of crashers deleted
                        int crashersDeleted = Integer.parseInt(Files.readAllLines(Paths.get("counter.txt")).get(0));
                        fileWriter = new FileWriter("counter.txt");
                        fileWriter.write(String.valueOf(crashersDeleted + 1));
                        fileWriter.close();
                        break;
                    }
                }
            }
            if (line.contains("pix_fmt")) {
                String num = line.replaceAll(",", "").replaceAll("\\s+","");
                if (pixFMT == null) {
                    pixFMT = num;
                } else {
                    if (!pixFMT.equals(num)) {

                        if(!event.getGuild().getSelfMember().getPermissions().contains(Permission.MESSAGE_MANAGE)){
                            event.getChannel().sendMessage("CRASHER FOUND, I am missing the `MESSAGE_MANAGE` permission! This permission is critical for the bot to function.\nMessage Link:" +
                                    "https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessage().getId() + "\nPosted by: "
                                    + event.getAuthor().getAsMention()).queue();
                        }

                        //Delete message and log to channel
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage("Crasher removed! Posted by " + event.getMessage().getAuthor().getAsMention() + "\n" +
                                "Video Format changed from `" + pixFMT.substring(pixFMT.indexOf(":") + 2) + "` to `" + num.substring(num.indexOf(":") + 2) + "`").queue();

                        //Keep track of crashers deleted
                        int crashersDeleted = Integer.parseInt(Files.readAllLines(Paths.get("counter.txt")).get(0));
                        fileWriter = new FileWriter("counter.txt");
                        fileWriter.write(String.valueOf(crashersDeleted + 1));
                        fileWriter.close();
                        break;
                    }
                }
            }
        }
    }

}
