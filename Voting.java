package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Voting extends ListenerAdapter {

    private final String[] allowedReactions = {"game_sb:1089592353645412482",
            "game_bb:1089592675595984986", "game_hitw:1089592541663469678", "game_tgttos:1089592804696653906"};

    String getBar(int percentage) {
        StringBuilder bar = new StringBuilder("[");
        if (percentage == 0) {
            bar.append("░░░░░░░░░░");
        } else if (percentage % 10 == 0) {
            bar.append("▓".repeat(percentage / 10));
            bar.append("░".repeat(10 - (percentage / 10)));
        } else {
            bar.append("▓".repeat(percentage / 10));
            bar.append("▒");
            bar.append("░".repeat(9 - ((percentage / 10))));
        }
        bar.append("]");
        return bar.toString();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("vote-begin")) {
            event.deferReply().queue();
            boolean skyBattle = Objects.requireNonNull(event.getOption("sky_battle")).getAsBoolean();
            boolean battleBox = Objects.requireNonNull(event.getOption("battle_box")).getAsBoolean();
            boolean holeInTheWall = Objects.requireNonNull(event.getOption("hole_in_the_wall")).getAsBoolean();
            boolean toGetToTheOtherSide = Objects.requireNonNull(event.getOption("to_get_to_the_other_side")).getAsBoolean();
            int duration = Objects.requireNonNull(event.getOption("duration")).getAsInt();
            String roleId = event.getOption("role_id") != null ? Objects.requireNonNull(event.getOption("role_id")).getAsString() : null;
            String title = Objects.requireNonNull(event.getOption("title")).getAsString();

            // Build the poll message
            EmbedBuilder pollEmbedBuilder = new EmbedBuilder();
            pollEmbedBuilder.setTitle(title);

            // Add the vote options to the message and the corresponding reactions
            List<Emoji> reactionEmojis = new ArrayList<>();
            if (skyBattle) {
                reactionEmojis.add(Emoji.fromFormatted("<:game_sb:1089592353645412482>")); // SB Emoji
            }
            if (battleBox) {
                reactionEmojis.add(Emoji.fromFormatted("<:game_bb:1089592675595984986>")); // BB Emoji
            }
            if (holeInTheWall) {
                reactionEmojis.add(Emoji.fromFormatted("<:game_hitw:1089592541663469678>")); // HITW Emoji
            }
            if (toGetToTheOtherSide) {
                reactionEmojis.add(Emoji.fromFormatted("<:game_tgttos:1089592804696653906")); // TGTTOS Emoji
            }

            // Send the poll message and add the reactions
            event.getHook().sendMessageEmbeds(pollEmbedBuilder.build()).queue(message -> {
                for (Emoji emoji : reactionEmojis) {
                    message.addReaction(emoji).queue();
                }

                // Add the poll end time to the message
                Instant pollEndTime = Instant.now().plus(duration, ChronoUnit.MINUTES);
                String pollEndTimeString = "<t:" + pollEndTime.getEpochSecond() + ":R>";
                pollEmbedBuilder.addField("Details", "Poll ends " + pollEndTimeString, false);
                message.editMessageEmbeds(pollEmbedBuilder.build()).queue();

                // Listen for reactions and update the poll message accordingly
                String messageID = message.getId();
                TextChannel channel = event.getChannel().asTextChannel();
                sendAndUpdatePollMessage(skyBattle, battleBox, holeInTheWall, toGetToTheOtherSide, pollEmbedBuilder, messageID, channel, true);
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(() -> {
                    sendAndUpdatePollMessage(skyBattle, battleBox, holeInTheWall, toGetToTheOtherSide, pollEmbedBuilder, messageID, channel, false);
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader("voteID.txt"));
                        String fileMessageID = reader.readLine();
                        reader.close();
                        Message pollMessage = channel.retrieveMessageById(fileMessageID).complete();
                        List<MessageReaction> reactions = pollMessage.getReactions();
                        int totalReactions = 0;
                        int skyBattleVotes = 0;
                        int battleBoxVotes = 0;
                        int holeInWallVotes = 0;
                        int toGetToOtherSideVotes = 0;
                        // Count the number of reactions for each vote option
                        for (MessageReaction reaction : reactions) {
                            Emoji reactionCode = reaction.getEmoji();
                            if (reactionCode.equals(Emoji.fromFormatted("<:game_sb:1089592353645412482>"))) { // SB Emoji
                                skyBattleVotes = reaction.getCount() - 1; // Subtract 1 to exclude the bot's reaction
                            } else if (reactionCode.equals(Emoji.fromFormatted("<:game_bb:1089592675595984986>"))) { // BB Emoji
                                battleBoxVotes = reaction.getCount() - 1;
                            } else if (reactionCode.equals(Emoji.fromFormatted("<:game_hitw:1089592541663469678>"))) { // HITW Emoji
                                holeInWallVotes = reaction.getCount() - 1;
                            } else if (reactionCode.equals(Emoji.fromFormatted("<:game_tgttos:1089592804696653906>"))) { // TGTTOS Emoji
                                toGetToOtherSideVotes = reaction.getCount() - 1;
                            }
                            totalReactions += reaction.getCount() - 1; // Subtract 1 to exclude the bot's reaction
                        }
                        EmbedBuilder winnerEmbed = new EmbedBuilder();
                        String winner;
                        if (skyBattleVotes > battleBoxVotes && skyBattleVotes > holeInWallVotes && skyBattleVotes > toGetToOtherSideVotes)
                            winner = "<:game_sb:1089592353645412482> - Sky Battle";
                        else if (battleBoxVotes > skyBattleVotes && battleBoxVotes > holeInWallVotes && battleBoxVotes > toGetToOtherSideVotes)
                            winner = "<:game_bb:1089592675595984986> - Battle Box";
                        else if (holeInWallVotes > skyBattleVotes && holeInWallVotes > battleBoxVotes && holeInWallVotes > toGetToOtherSideVotes)
                            winner = "<:game_hitw:1089592541663469678> - Hole In The Wall";
                        else if (toGetToOtherSideVotes > battleBoxVotes && toGetToOtherSideVotes > holeInWallVotes && toGetToOtherSideVotes > skyBattleVotes)
                            winner = "<:game_tgttos:1089592804696653906> - To Get To The Other Side";
                        else winner = "More than one game. As a result, a wheel will be spun with these games on it.";
                        winnerEmbed.setTitle("<:crown:1089595885240975522>** Winner of the vote is:**");
                        winnerEmbed.setDescription("> **" + winner + "**");
                        winnerEmbed.addField("<:star:1089595629401026600>** Total votes cast:** ", Integer.toString(totalReactions), false);
                        channel.sendMessageEmbeds(winnerEmbed.build()).queue();
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("voteID.txt", false));
                            writer.write("");
                            writer.close();
                        } catch (IOException ignored) {
                        }
                    } catch (IOException ignored) {
                    }
                }, duration, TimeUnit.MINUTES);
            });
            event.getJDA().addEventListener(new ListenerAdapter() {
                @Override
                public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
                    String messageID = event.getMessageId();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader("voteID.txt"));
                        String fileMessageID = reader.readLine();
                        if (!messageID.equals(fileMessageID)) {
                            System.out.println("Not valid message!");
                            return;
                        }
                    } catch (IOException ignored) {
                    }
                    boolean containsTarget = false;
                    for (String reaction : allowedReactions) {
                        if (event.getEmoji().getAsReactionCode().equals(reaction)) {
                            containsTarget = true;
                            break;
                        }
                    }
                    if (!containsTarget) {
                        System.out.println("Returned due to irrelevant reaction");
                        return;
                    }
                    if (Objects.requireNonNull(event.getUser()).isBot()) {
                        System.out.println("Returned due to incorrect user");
                        return;
                    }
                    if (roleId == null || Objects.requireNonNull(event.getMember()).getRoles().contains(event.getJDA().getRoleById(roleId))) {
                        TextChannel channel = event.getChannel().asTextChannel();
                        sendAndUpdatePollMessage(skyBattle, battleBox, holeInTheWall, toGetToTheOtherSide, pollEmbedBuilder, messageID, channel, false);
                    } else {
                        event.getReaction().removeReaction().queue();
                    }
                }
            });
            event.getJDA().addEventListener(new ListenerAdapter() {
                @Override
                public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
                    String messageID = event.getMessageId();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader("voteID.txt"));
                        String fileMessageID = reader.readLine();
                        if (!messageID.equals(fileMessageID)) {
                            System.out.println("Not valid message!");
                            return;
                        }
                    } catch (IOException ignored) {
                    }
                    TextChannel channel = event.getChannel().asTextChannel();
                    sendAndUpdatePollMessage(skyBattle, battleBox, holeInTheWall, toGetToTheOtherSide, pollEmbedBuilder, messageID, channel, false);
                }
            });
        }
    }

    void sendAndUpdatePollMessage(boolean skyBattle, boolean battleBox, boolean holeInTheWall, boolean toGetToTheOtherSide, EmbedBuilder pollEmbedBuilder, String messageID, TextChannel channel, boolean Start) {
        //System.out.println(Start);
        if (!Start) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("voteID.txt"));
                String fileMessageID = reader.readLine();
                if (!messageID.equals(fileMessageID)) {
                    System.out.println("Not valid message!");
                    return;
                }
            } catch (IOException ignored) {
            }
        }
        //System.out.println("yo");
        channel.retrieveMessageById(messageID).queue(pollMessage -> {
            //System.out.println("day");
            if (Start) {
                //System.out.println("Added 1");
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("voteID.txt", false));
                    writer.write(pollMessage.getId());
                    writer.close();
                    //System.out.println("Added 2");
                } catch (IOException e) {
                    //System.out.println("Failed to add");
                }
            }
            //System.out.println("Sent");
            List<MessageReaction> reactions = pollMessage.getReactions();
            int totalReactions = 0;
            int skyBattleVotes = 0;
            int battleBoxVotes = 0;
            int holeInWallVotes = 0;
            int toGetToOtherSideVotes = 0;

            // Count the number of reactions for each vote option
            for (MessageReaction reaction : reactions) {
                Emoji reactionCode = reaction.getEmoji();
                if (reactionCode.equals(Emoji.fromFormatted("<:game_sb:1089592353645412482>"))) { // SB Emoji
                    skyBattleVotes = reaction.getCount() - 1; // Subtract 1 to exclude the bot's reaction
                } else if (reactionCode.equals(Emoji.fromFormatted("<:game_bb:1089592675595984986>"))) { // BB Emoji
                    battleBoxVotes = reaction.getCount() - 1;
                } else if (reactionCode.equals(Emoji.fromFormatted("<:game_hitw:1089592541663469678>"))) { // HITW Emoji
                    holeInWallVotes = reaction.getCount() - 1;
                } else if (reactionCode.equals(Emoji.fromFormatted("<:game_tgttos:1089592804696653906>"))) { // TGTTOS
                    toGetToOtherSideVotes = reaction.getCount() - 1;
                }
                totalReactions += reaction.getCount() - 1; // Subtract 1 to exclude the bot's reaction
            }


            // Calculate the percentage of votes for each vote option
            int skyBattlePercentage = 0;
            int battleBoxPercentage = 0;
            int holeInWallPercentage = 0;
            int toGetToOtherSidePercentage = 0;

            if (totalReactions > 0) {
                skyBattlePercentage = skyBattleVotes * 100 / totalReactions;
                battleBoxPercentage = battleBoxVotes * 100 / totalReactions;
                holeInWallPercentage = holeInWallVotes * 100 / totalReactions;
                toGetToOtherSidePercentage = toGetToOtherSideVotes * 100 / totalReactions;
            }

            // Update the poll message with the new vote counts and percentages
            String pollMessageString = "";
            if (skyBattle) {
                pollMessageString += String.format("**Sky Battle**  <:game_sb:1089592353645412482> %s [ %s ]\n\n", getBar(skyBattlePercentage), (skyBattlePercentage + "% • " + skyBattleVotes));
            }
            if (battleBox) {
                pollMessageString += String.format("**Battle Box**  <:game_bb:1089592675595984986> %s [ %s ]\n\n", getBar(battleBoxPercentage), (battleBoxPercentage + "% • " + battleBoxVotes));
            }
            if (holeInTheWall) {
                pollMessageString += String.format("**Hole In Wall**  <:game_hitw:1089592541663469678> %s [ %s ]\n\n", getBar(holeInWallPercentage), (holeInWallPercentage + "% • " + holeInWallVotes));
            }
            if (toGetToTheOtherSide) {
                pollMessageString += String.format("**To Get To The Other Side**  <:game_tgttos:1089592804696653906> %s [ %s ]\n\n", getBar(toGetToOtherSidePercentage), (toGetToOtherSidePercentage + "% • " + toGetToOtherSideVotes));
            }
            pollMessageString += String.format("Total Votes: %d", totalReactions);
            pollEmbedBuilder.setDescription(pollMessageString);
            try {
                pollMessage.editMessageEmbeds(pollEmbedBuilder.build()).queue();
            } catch (IllegalStateException ignored) {
            }
        });
        //System.out.println("meow");
    }
}