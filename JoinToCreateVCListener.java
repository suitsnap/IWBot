package listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class JoinToCreateVCListener extends ListenerAdapter {
    private final Map<Long, VoiceChannel> userToVCMap = new HashMap<>();
    private int channelCount = 0;

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        User user = event.getMember().getUser();
        AudioChannel oldChannel = event.getOldValue();
        // channel ID for Join to Create channel
        long joinToCreateChannelId = 1096110594077237399L;
        try {
            if (event.getChannelJoined().getIdLong() == joinToCreateChannelId) {
                Guild guild = event.getGuild();
                VoiceChannel vc = userToVCMap.get(user.getIdLong());

                if (vc != null) {
                    // User already has a temporary VC, move them to it
                } else {
                    // Create new temporary VC
                    vc = createNewVC(guild);

                    // Move user to new VC
                    guild.moveVoiceMember(event.getMember(), vc).queue();

                    // Add user to VC map
                    userToVCMap.put(user.getIdLong(), vc);
                }
            }
        } catch (NullPointerException ignoredError) {

        }
        if (oldChannel != null) {
            if (oldChannel.equals(userToVCMap.get(user.getIdLong()))) {
                userToVCMap.remove(user.getIdLong());
            }
            if (oldChannel.getParentCategory().getId().equals("1086418807293219006")
                    && !oldChannel.getId().equals("1096110594077237399") && !oldChannel.getId().equals("1086419539153137747")
                    && !oldChannel.getId().equals("1086419583050723418") && oldChannel.getMembers().isEmpty()) {
                event.getChannelLeft().delete().queue();
                channelCount--;
            }
        }

    }

    private VoiceChannel createNewVC(Guild guild) {
        // Increment channel count and create new VC with incremented name
        channelCount++;
        // category ID for Join to Create VCs
        long CATEGORY_ID = 1086418807293219006L;
        VoiceChannel vc = guild.createVoiceChannel("VC " + channelCount)
                .setParent(guild.getCategoryById(CATEGORY_ID))
                .complete();

        // Set maximum number of members for the VC
        int maxMembersPerVC = 10;
        vc.getManager().setUserLimit(maxMembersPerVC).queue();
        return vc;
    }
}

