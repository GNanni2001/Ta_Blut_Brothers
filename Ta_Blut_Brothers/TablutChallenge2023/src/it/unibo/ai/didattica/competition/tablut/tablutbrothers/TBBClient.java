package it.unibo.ai.didattica.competition.tablut.tablutbrothers;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.AIMA_GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * We know you're reading this, DO NOT COPY THIS FILE!
 * Just kidding you clearly can, but we sucked, enjoy the challenge!
 *
 * 
 * @author <b>Team Ta_Blut_Brothers</b>
 */
public class TBBClient extends TablutClient {
    public static final String TEAM_NAME = "Ta_Blut_Brothers";
    public static final String PLAYER_NAME = "Bod";

    private int game;
    private boolean debug;

    /**
     * Main constructor
     *
     * @param player    the side the player plays for [WHITE | BLACK]
     * @param name      the name of the team
     * @param timeout   the maximum number of seconds the player has to choose its next move
     * @param ipAddress the server address
     * @param game      the game rules [1 - BasicTablut | 2 - ModernTablut | 3 - BrandubTablut | <strong>4 - AshtonTablut</strong>]
     * @param debug     enable debug prints (show the depth of the search and information about the expanded nodes)
     *
     * @throws UnknownHostException if the IP address of the host could not be determined.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public TBBClient(String player, String name, int timeout, String ipAddress, int game, boolean debug) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
        this.game = game;
        this.debug = debug;
    }
    public TBBClient(String player) throws IOException {
        super(player, TEAM_NAME, 60, "localhost");
        this.game = 4;
        this.debug = false;
    }

    public static void main(String[] args) throws IOException {
        int gameType = 4;
        String role = "";
        String ip = "localhost";
        int timeout = 60;
        boolean deb = false;

        if(args.length < 1) {
            System.out.printf("ERROR: You must specify which player you are [WHITE | BLACK]\n" +
                    "\tUSAGE: ./runmyplayer <black|white> <timeout-in-seconds> <server-ip> <debug>\n");
            System.exit(1);
        } else {
            role = (args[0]);
        }

        if(args.length == 2) {
            try {
                timeout = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.printf("ERROR: Timeout must be an integer representing seconds\n" +
                        "\tUSAGE: ./runmyplayer <black|white> <timeout-in-seconds> <server-ip> <debug>\n");
                System.exit(1);
            }
        }

        if(args.length == 3) {
            try {
                timeout = Integer.parseInt(args[1]);
                ip = args[2];
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.printf("ERROR: Timeout must be an integer representing seconds\n" +
                        "\tUSAGE: ./runmyplayer <black|white> <timeout-in-seconds> <server-ip> <debug>\n");
                System.exit(1);
            }
        }

        if(args.length == 4) {
            try {
                timeout = Integer.parseInt(args[1]);
                ip = args[2];
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.printf("ERROR: Timeout must be an integer representing seconds\n" +
                        "\tUSAGE: ./runmyplayer <black|white> <timeout-in-seconds> <server-ip> <debug>\n");
                System.exit(1);
            }

            if(args[3].equals("debug")) {
                deb = true;
            } else {
                System.out.printf("ERROR: The last argument can be only 'debug' and it allow to print logs during search\n" +
                        "\tUSAGE: ./runmyplayer <black|white> <timeout-in-seconds> <server-ip> <debug>");
                System.exit(1);
            }
        }

        System.out.println("Team Ta_Blut_Brothers\n\nMembers:\n\tDavide Cremonini\n\tGabriele Nanni\n");
        System.out.println("Player:  " + role);
        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Server:  " + ip);
        System.out.println("Debug:   " + deb + "\n");

        TBBClient client = new TBBClient(role, PLAYER_NAME + " (Team " + TEAM_NAME + ")", timeout, ip, gameType, deb);
        client.run();
    }

    @Override
    public void run() {
       
        // Declare name to the server
        try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        State state = new StateTablut();
        state.setTurn(State.Turn.WHITE); // WHITE makes the first move

        AIMA_GameAshtonTablut tablut = new AIMA_GameAshtonTablut(3,-1, "logs", "white_ai", "black_ai");

        while(true) {
            try {
                // Receive the state from the Server
                this.read();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                System.exit(2);
            }

            // Update the state received
            state = this.getCurrentState();
            System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME  + "Says: \"Say my Name\" ");
            System.out.println(state.toString());

            // WHITE turn
            if(this.getPlayer().equals(State.Turn.WHITE)) {

                if(state.getTurn().equals(State.Turn.WHITE)) {

                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME +  " Says: \"Better Call Saul! \" ");

                    Action a = findBestMove(tablut, state);

                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME  + " Says: \"Run!\" " + a.toString() + "");

                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                } else if(state.getTurn().equals(State.Turn.BLACK)) {
                    // Opponent TURN
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"There will be consequences.\" ");
                } else if(state.getTurn().equals(State.Turn.WHITEWIN)) {
                    // if WHITE win
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"It's over...I won.\" ");
                    System.exit(0);
                } else if(state.getTurn().equals(State.Turn.BLACKWIN)) {
                    // if BLACK win
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"Ding! Ding! Ding! ... UP!!! \"");
                    System.exit(0);
                } else if(state.getTurn().equals(State.Turn.DRAW)) {
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"We are done, when i say we are done\" ");
                    System.exit(0);
                }
            }
            // BLACK turn
            else {

                if(state.getTurn().equals(State.Turn.BLACK)) {

                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME +  " Says: \"Better Call Saul! \" ");

                    Action a = findBestMove(tablut, state);

                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME  + " Says: \"Run!\" " + a.toString() + "");

                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                } else if(state.getTurn().equals(State.Turn.WHITE)) {
                    // Opponent TURN
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"There will be consequences.\" ");
                } else if(state.getTurn().equals(State.Turn.BLACKWIN)) {
                    // if WHITE win
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"It's over...I won.\" ");
                    System.exit(0);
                } else if(state.getTurn().equals(State.Turn.WHITEWIN)) {
                    // if BLACK win
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"Ding! Ding! Ding! ... UP!!! \"");
                    System.exit(0);
                } else if(state.getTurn().equals(State.Turn.DRAW)) {
                    System.out.println(TEAM_NAME.toUpperCase() + ": " + PLAYER_NAME + " Says: \"We are done, when i say we are done\" ");
                    System.exit(0);
                }

            }
        }
    }

    private Action findBestMove(AIMA_GameAshtonTablut tablutGame, State state) {

        TBBSearch search = new TBBSearch(tablutGame, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, super.getTimeout() - 2);
        search.setLogEnabled(debug);
        return search.makeDecision(state);
    }

}
