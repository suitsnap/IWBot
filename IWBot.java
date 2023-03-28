import io.github.cdimascio.dotenv.Dotenv;
import listeners.ButtonRoles;
import listeners.Rules;
import listeners.TicketSystem;
import listeners.Voting;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class IWBot {

    public IWBot() throws LoginException {
        Dotenv config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("MCC Island."));
        builder.enableIntents(EnumSet.allOf(GatewayIntent.class));
        JDA jda = builder.build();
        jda.addEventListener(new TicketSystem(), new ButtonRoles(), new Rules(), new Voting());
    }

    public static void main(String[] args) {

        try {
            new IWBot();
        } catch (LoginException e) {
            System.out.println("ERROR: Login failed due to invalid token.");
        }
    }

}