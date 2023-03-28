package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ButtonRoles extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(event.getJDA().getRoleById(1086084386790850630L))).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("role-enable")) {
            // get category
            // create buttons
            // game roles
            Button roleButtonBB = Button.secondary("role-button-bb", "Battle Box").withEmoji(Emoji.fromFormatted("<:game_bb:1089592675595984986>"));
            Button roleButtonSB = Button.secondary("role-button-sb", "Sky Battle").withEmoji(Emoji.fromFormatted("<:game_sb:1089592353645412482>"));
            Button roleButtonHITW = Button.secondary("role-button-hitw", "Hole In The Wall").withEmoji(Emoji.fromFormatted("<:game_hitw:1089592541663469678>"));
            Button roleButtonTGTTOS = Button.secondary("role-button-tgttos", "To Get To The Other Side").withEmoji(Emoji.fromFormatted("<:game_tgttos:1089592804696653906>"));
            // pronoun roles
            Button roleButtonHH = Button.secondary("role-button-hh", "He/Him");
            Button roleButtonHT = Button.secondary("role-button-ht", "He/They");
            Button roleButtonSH = Button.secondary("role-button-sh", "She/Her");
            Button roleButtonST = Button.secondary("role-button-st", "She/They");
            Button roleButtonTT = Button.secondary("role-button-tt", "They/Them");
            Button roleButtonAskP = Button.secondary("role-button-askP", "Ask Pronouns");
            Button roleButtonAnyP = Button.secondary("role-button-anyP", "Any Pronouns");
            Button roleButtonUN = Button.secondary("role-button-un", "Use Name");
            // ping roles
            Button roleButtonNewsPing = Button.secondary("role-button-newsPing", "Weekly Warrior Pings");
            Button roleButtonUpdatePing = Button.secondary("role-button-updatePing", "MCCI Update Pings");
            Button roleButtonServerPing = Button.secondary("role-button-serverPing", "Server Update Pings");
            Button roleButtonEventPing = Button.secondary("role-button-eventPing", "Events Ping");
            Button roleButtonAnnouncePing = Button.secondary("role-button-announcePing", "Announcements Ping");
            // create main embed
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("React Roles");
            embed.setColor(new Color(0xd60000));
            embed.setDescription("This is the way to gain roles! Below are buttons that you can press and it will add or remove the role relating to the button you press.");
            // create game embed
            EmbedBuilder gameEmbed = new EmbedBuilder();
            gameEmbed.setTitle("Game Roles");
            gameEmbed.setColor(new Color(0xd60000));
            gameEmbed.setDescription("These roles are for the MCCI games and will give you a role that can be pinged when someone wants to look for a party for that specific game.");
            // create pronoun embed
            EmbedBuilder pronounEmbed = new EmbedBuilder();
            pronounEmbed.setTitle("Pronoun Roles");
            pronounEmbed.setColor(new Color(0xd60000));
            pronounEmbed.setDescription("Feel free to define yourself with these pronoun roles! If your preferred pronouns aren't shown below, please open a ticket and we'll happily resolve this issue!");
            // create ping embed
            EmbedBuilder pingEmbed = new EmbedBuilder();
            pingEmbed.setTitle("Ping Roles");
            pingEmbed.setColor(new Color(0xd60000));
            pingEmbed.setDescription("Want to keep up to date with everything? Click the buttons below to add roles for all the great news in IW!");
            pingEmbed.setFooter("Made by SuitSnap", "https://static-cdn.jtvnw.net/jtv_user_pictures/6df3a537-0cc0-41f0-b074-04eb81c7589f-profile_image-70x70.png");
            // send messages
            event.reply("Message sent, king!").setEphemeral(true).queue();
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
            event.getChannel().sendMessageEmbeds(gameEmbed.build()).addActionRow(roleButtonBB, roleButtonSB, roleButtonHITW, roleButtonTGTTOS).queue();
            event.getChannel().sendMessageEmbeds(pronounEmbed.build()).addActionRow(roleButtonHH, roleButtonHT, roleButtonSH, roleButtonST, roleButtonTT).addActionRow(roleButtonAskP, roleButtonAnyP, roleButtonUN).queue();
            event.getChannel().sendMessageEmbeds(pingEmbed.build()).addActionRow(roleButtonNewsPing, roleButtonUpdatePing, roleButtonServerPing, roleButtonEventPing, roleButtonAnnouncePing).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonName = event.getButton().getId();
        List<Role> memberByRoles = Objects.requireNonNull(event.getMember()).getRoles();
        switch (Objects.requireNonNull(buttonName)) {
            case "role-button-bb" -> {
                Role role = event.getJDA().getRoleById("1055218599331115088");
                if (!memberByRoles.contains(role)) {
                    Objects.requireNonNull(event.getGuild()).addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Battle Box Role!").setEphemeral(true).queue();
                } else {
                    Objects.requireNonNull(event.getGuild()).removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Battle Box Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-sb" -> {
                Role role = event.getJDA().getRoleById("1055218607338045530");
                if (!memberByRoles.contains(role)) {
                    Objects.requireNonNull(event.getGuild()).addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Sky Battle Role!").setEphemeral(true).queue();
                } else {
                    Objects.requireNonNull(event.getGuild()).removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Sky Battle Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-hitw" -> {
                Role role = event.getJDA().getRoleById("1055218613382037554");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Hole In The Wall Role!").setEphemeral(true).queue();
                } else {
                    Objects.requireNonNull(event.getGuild()).removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Hole In The Wall Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-tgttos" -> {
                Role role = event.getJDA().getRoleById("1055218616154464298");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added To Get To The Other Side Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed To Get To The Other Side Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-hh" -> {
                Role role = event.getJDA().getRoleById("1055217419154632725");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added He/Him Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed He/Him Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-ht" -> {
                Role role = event.getJDA().getRoleById("1055217556035735592");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added He/They Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed He/They Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-sh" -> {
                Role role = event.getJDA().getRoleById("1055217425966170133");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added She/Her Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed She/Her Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-st" -> {
                Role role = event.getJDA().getRoleById("1055217477824557138");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added She/They Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed She/They Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-tt" -> {
                Role role = event.getJDA().getRoleById("1055217386254508165");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added They/Them Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed They/Them Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-askP" -> {
                Role role = event.getJDA().getRoleById("1055217649656803368");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Ask Pronouns Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Ask Pronouns Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-anyP" -> {
                Role role = event.getJDA().getRoleById("1055218190373900298");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Any Pronouns Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Any Pronouns Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-un" -> {
                Role role = event.getJDA().getRoleById("1055217658611646524");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Use Name Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Use Name Role!").setEphemeral(true).queue();
                }

            }
            case "role-button-newsPing" -> {
                Role role = event.getJDA().getRoleById("1086386709803061330");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Weekly Warrior Pings Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Weekly Warrior Pings Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-updatePing" -> {
                Role role = event.getJDA().getRoleById("1086386917177831505");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added MCCI Updates Ping Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed MCCI Updates Ping Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-serverPing" -> {
                Role role = event.getJDA().getRoleById("1086387014368239746");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Server Updates Ping Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Server Updates Ping Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-eventPing" -> {
                Role role = event.getJDA().getRoleById("1087394929849946132");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Event Pings Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Event Pings Role!").setEphemeral(true).queue();
                }
            }
            case "role-button-announcePing" -> {
                Role role = event.getJDA().getRoleById("1087394872207622165");
                if (!memberByRoles.contains(role)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Added Announcement Pings Role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(event.getUser().getId()), Objects.requireNonNull(role)).queue();
                    event.reply("Removed Announcement Pings Role!").setEphemeral(true).queue();
                }
            }
        }
    }
}
