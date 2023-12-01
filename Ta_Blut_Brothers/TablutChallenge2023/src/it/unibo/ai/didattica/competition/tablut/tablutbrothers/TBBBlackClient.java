package it.unibo.ai.didattica.competition.tablut.tablutbrothers;

import java.io.IOException;
import java.net.UnknownHostException;

public class TBBBlackClient {

    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
        String[] arguments = new String[]{"BLACK", "60", "localhost", "debug"}; // optional debug

        if(args.length > 0) {
            arguments = new String[]{"BLACK", args[0]};
        }

        TBBClient.main(arguments);
    }
}