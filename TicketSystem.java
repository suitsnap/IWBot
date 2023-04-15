package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TicketSystem extends ListenerAdapter {
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("ticket-enable")) {
            // get category
            Category category = event.getJDA().getCategoryById("1054887073884151898");
            // create button
            Button ticketButton = Button.primary("ticket-button", "\uD83D\uDCE9");
            // create embed?
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Tickets");
            embed.setColor(new Color(0xd60000));
            embed.setDescription("This is used to open a ticket if you have any problems whether that be during a tournament or signing up, anything you have a problem with (relating to this server).\n To open a ticket click the button with the emoji '\uD83D\uDCE9' and navigate to the channel specific to you!");
            embed.setFooter("Made by SuitSnap", "https://static-cdn.jtvnw.net/jtv_user_pictures/6df3a537-0cc0-41f0-b074-04eb81c7589f-profile_image-70x70.png");
            // send message
            event.reply("Message sent, king!").setEphemeral(true).queue();
            event.getChannel().sendMessageEmbeds(embed.build()).setActionRow(ticketButton).queue();

        } else if (command.equals("purge")){
            String[] teamchannels =
                    {"1092171727892643922","1093935763240001586"
                    ,"1093935837521125506","1093935500181651466"
                    ,"1093711207837466644","1093711316650315776"
                    ,"1093711389228548316","1093711610616492113"
                    ,"1093711717931946034","1093712245592170597"};
            for (String channel : teamchannels){
                event.getGuild().getTextChannelById(channel).getIterableHistory().forEach(message -> message.delete().queue());
            }
            event.reply("All messages are being purged!").setEphemeral(true).queue(); // Sends a confirmation message

        } else if (command.equals("team-initiate")){
            event.reply("Sending now!").setEphemeral(true).queue();
            //Red Rabbits
            event.getGuild().getTextChannelById("1092171727892643922").sendMessage(
                    "<@&1092166116157173860>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1092160418270695564").queue();
            //Orange Ocelots
            event.getGuild().getTextChannelById("1093935763240001586").sendMessage(
                    "<@&1092166991571337296>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1092160481445289995").queue();
            //Yellow Yaks
            event.getGuild().getTextChannelById("1093935837521125506").sendMessage(
                    "<@&1092171089632841878>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1092160544485671073").queue();
            //Lime Llamas
            event.getGuild().getTextChannelById("1093935500181651466").sendMessage(
                    "<@&1092190734477709315>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1092191134031302726").queue();
            //Green Geckos
            event.getGuild().getTextChannelById("1093711207837466644").sendMessage(
                    "<@&1093708909925122059>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1093713504969695282").queue();
            //Cyan Coyotes
            event.getGuild().getTextChannelById("1093711316650315776").sendMessage(
                    "<@&1093709536319254559>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1093713641049698354").queue();
            //Aqua Axolotls
            event.getGuild().getTextChannelById("1093711389228548316").sendMessage(
                    "<@&1093709616241713192>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1093713747324973158").queue();
            //Blue Bats
            event.getGuild().getTextChannelById("1093711610616492113").sendMessage(
                    "<@&1093709718540795954>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1093713826798653621").queue();
            //Purple Pandas
            event.getGuild().getTextChannelById("1093711717931946034").sendMessage(
                    "<@&1093709792012415067>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1093713936739741746").queue();
            //Pink Parrots
            event.getGuild().getTextChannelById("1093712245592170597").sendMessage(
                    "<@&1093709868847870052>, please check you can see this message and the voice channel: https://discord.com/channels/1052015794395037776/1093714001726283877").queue();

        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("ticket-button")) {
            event.deferReply().setEphemeral(true).queue();
            Category category = event.getJDA().getCategoryById("1054887073884151898");
            String channelName = "ticket-" + event.getUser().getName().toLowerCase();
            category.createTextChannel(channelName).queue(textChannel -> {
                String channelID = textChannel.getId();
                event.getHook().sendMessage("Ticket created! Please go to: " + event.getGuild().getTextChannelById(channelID).getAsMention()).queue();
                Member member = event.getMember();
                event.getGuild().getTextChannelById(channelID).upsertPermissionOverride(member).grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND).queue();
                wait(500);
                Button closeButton = Button.danger("close-button", "Close ticket \uD83D\uDD12");
                textChannel.sendMessage("Hi there, " + event.getUser().getAsMention() + ", welcome to your ticket. Here " + event.getGuild().getRoleById("1052026521356873788").getAsMention() + " can provide any necessary information or help! Once you are ready to close the ticket, just press the button below!").setActionRow(closeButton).queue();

            });

        }
        if (event.getButton().getId().equals("close-button")) {
            event.getChannel().delete().queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("purge", "Purges a channel")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
        commandData.add(Commands.slash("team-initiate", "Sends all the pings for team chats")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
        commandData.add(Commands.slash("ticket-enable", "Create the message for the ticket system.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        commandData.add(Commands.slash("role-enable", "Create the message for the role adding system.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        commandData.add(Commands.slash("rule-enable", "Create the message for the rules.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        commandData.add(Commands.slash("vote-begin", "Starts a vote")
                .addOption(OptionType.STRING, "title", "Title of the poll", true)
                .addOption(OptionType.INTEGER, "duration", "How long should the vote last in minutes?", true)
                .addOption(OptionType.BOOLEAN, "sky_battle", "Include Sky Battle in the vote?", true)
                .addOption(OptionType.BOOLEAN, "battle_box", "Include Battle Box in the vote?", true)
                .addOption(OptionType.BOOLEAN, "hole_in_the_wall", "Include Hole In The Wall in the vote", true)
                .addOption(OptionType.BOOLEAN, "to_get_to_the_other_side", "Include To Get To The Other Side in the vote?", true)
                .addOption(OptionType.STRING, "role_id", "Optional role ID that restricts who can vote", false)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
