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
        commandData.add(Commands.slash("ticket-enable", "Create the message for the ticket system.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        commandData.add(Commands.slash("role-enable", "Create the message for the role adding system.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        commandData.add(Commands.slash("rule-enable", "Create the message for the rules.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        commandData.add(Commands.slash("vote-begin","Start voting")
                .addOption(OptionType.BOOLEAN,"skybattle", "Is Sky Battle in the round options?",true)
                .addOption(OptionType.BOOLEAN,"battlebox", "Is Battle Box in the round options?",true)
                .addOption(OptionType.BOOLEAN,"holeinthewall", "Hole In The Wall in the round options?",true)
                .addOption(OptionType.BOOLEAN,"togettotheotherside", "Is To Get To The Other Side in the round options?",true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        commandData.add(Commands.slash("vote-end","Stops votes from being able to be added and ends the vote")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER)));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
