package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class Rules extends ListenerAdapter {
    // create embed


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("rule-enable")) {
            event.deferReply().setEphemeral(true).queue();
            EmbedBuilder embed = new EmbedBuilder();
            EmbedBuilder embed1 = new EmbedBuilder();
            EmbedBuilder embed2 = new EmbedBuilder();
            EmbedBuilder embed3 = new EmbedBuilder();
            EmbedBuilder embed4 = new EmbedBuilder();
            embed.setTitle("<:purple_warn:1053681087664111616> Rules <:purple_warn:1053681087664111616>");
            embed.setColor(new Color(0x24A9E1));
            embed1.setColor(new Color(0x24A9E1));
            embed2.setColor(new Color(0x24A9E1));
            embed3.setColor(new Color(0x24A9E1));
            embed4.setColor(new Color(0x24A9E1));
            embed1.setTitle("<:no:1053674901560643664> No Bullying or Inappropriate Content");
            embed1.setDescription(
                    "<:red_warn:1053681038179700888> Inappropriate or Not Safe For Work (NSFW) content or language, including (but not limited to) racism, homophobia/transphobia, hate speech, sexual " +
                            "harassment/references, drug references or politics, is not allowed.\n<:yellow_warn:1053681036942393495> Swearing is permitted but don't be an idiot.");
            embed2.setTitle("<:no:1053674901560643664> No Trolling, Spamming or Random Pinging");
            embed2.setDescription(
                    "<:orange_warn:1053681088842694677> All forms of spamming should be avoided.\n<:red_warn:1053681038179700888> Trolling, baiting and witch hunts are also unacceptable." +
                            "\n<:orange_warn:1053681088842694677> Randomly pinging staff or fellow community members, especially when they’re offline or not involved in the chat," +
                            " is also not allowed, even if you know them in real life.");
            embed3.setTitle("<:no:1053674901560643664> No Inappropriate Usernames, Descriptions and Profile Pictures");
            embed3.setDescription("<:red_warn:1053681038179700888> NSFW or inappropriate usernames, descriptions and profile pictures are not allowed and will be removed or altered by staff.");
            embed4.setTitle("<:no:1053674901560643664> No Self-Promotion/Advertising or Impersonation");
            embed4.setDescription(
                    "<:orange_warn:1053681088842694677> Promoting your personal projects or social channels outside the " + event.getGuild().getTextChannelById(1052245780737564672L).getAsMention() +
                            " channel is not permitted. This includes asking members for help with building, drawing" +
                            "or coding on your project.\n<:yellow_warn:1053681036942393495> Pretending to be someone you are not is also against the rules as it can be harmful");
            embed4.setFooter("Made by SuitSnap", "https://static-cdn.jtvnw.net/jtv_user_pictures/6df3a537-0cc0-41f0-b074-04eb81c7589f-profile_image-70x70.png");
            // send message
            event.getHook().sendMessage("Message sent, king!").queue();
            event.getChannel().sendMessageEmbeds(embed.build(), embed1.build(), embed2.build(), embed3.build(), embed4.build()).queue();
        }
    }
}
