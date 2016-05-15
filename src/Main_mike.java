public class Main_mike {

    private static void playHumanVsHuman(int boardWidth, int boardHeight) {
        GameManagerInterface g = new GameManager();
        PlayerInterface r = new HumanPlayer();
        PlayerInterface b = new HumanPlayer();
        boolean ready = false;
        try {
            if (g.specifyPlayer(r, Piece.RED)) {
                if (g.specifyPlayer(b, Piece.BLUE)) {
                    if (g.boardSize(boardWidth, boardHeight)) {
                        ready = true;
                    }
                }
            }
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if (ready) {
            g.playGame();
        }
    }

    private static void playHumanVsComputer(int boardWidth, int boardHeight) {
        GameManagerInterface g = new GameManager();
        PlayerInterface h = new HumanPlayer();
        PlayerInterface c = new ComputerPlayer_mike();
        boolean ready = false;
        try {
            if (g.specifyPlayer(h, Piece.RED)) {
                if (g.specifyPlayer(c, Piece.BLUE)) {
                    ready = true;
                }
            }
            if (!g.boardSize(boardWidth, boardHeight)) {
                ready = false;
            }
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if (ready) {
            g.playGame();
        }
    }

    private static void playComputerVsComputer(int boardWidth, int boardHeight) {
        GameManagerInterface g = new GameManager();
        PlayerInterface r = new ComputerPlayer_mike();
        PlayerInterface b = new ComputerPlayer_mike();
        boolean ready = false;
        try {
            if (g.specifyPlayer(r, Piece.RED)) {
                if (g.specifyPlayer(b, Piece.BLUE)) {
                    if (g.boardSize(boardWidth, boardHeight)) {
                        ready = true;
                    }
                }
            }
        } catch (ColourAlreadySetException e) {
            System.out.println("That colour has already been set!");
        } catch (InvalidColourException e) {
            System.out.println("Invalid colour assigned to player!");
        } catch (BoardAlreadySizedException e) {
            System.out.println("Board has already been sized!");
        } catch (InvalidBoardSizeException e) {
            System.out.println("That's not a valid board size!");
        }
        if (ready) {
            g.playGame();
        }
    }

    private static float lastReport = 0.0f;

    private static void reportProgress(int done, int total) {
        float percentDone = 100 * (float)done / (float)total;
        if (Math.abs(percentDone - lastReport) > 0.25f) {
            String num = "" + percentDone;
            System.out.print(num.substring(0, Math.min(num.length(), 4)) + "% ");
            lastReport = percentDone;
        }
    }

    private static String singleMatch(int games, int width, int height, String playerAName, Class playerAPlayer, String playerBName, Class playerBPlayer) throws InstantiationException, IllegalAccessException {
        System.out.println(playerAName + " v " + playerBName);
        int totalGames = 2 * games;
        int aWin = 0, bWin = 0;
        //Player 1 goes first, first
        for (int game = 0; game < games; game++) {
            try {
                GameManager g = new GameManager();
                PlayerInterface pA = (PlayerInterface) playerAPlayer.newInstance();
                PlayerInterface pB = (PlayerInterface) playerBPlayer.newInstance();
                g.specifyPlayer(pA, Piece.RED);
                g.specifyPlayer(pB, Piece.BLUE);
                g.boardSize(width, height);
                g.playGame();
                if (g.whoWon == Piece.RED) {
                    aWin++;
                } else if (g.whoWon == Piece.BLUE) {
                    bWin++;
                }else{
                    System.out.println("NO WIN");
                }
                reportProgress(game, totalGames);
            } catch (Exception e) {
                System.out.println("Error: " + e.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
        //Now Player 2 goes first
        for (int game = games; game < totalGames; game++) {
            try {
                GameManager g = new GameManager();
                PlayerInterface pA = (PlayerInterface) playerAPlayer.newInstance();
                PlayerInterface pB = (PlayerInterface) playerBPlayer.newInstance();
                g.specifyPlayer(pA, Piece.BLUE);
                g.specifyPlayer(pB, Piece.RED);
                g.boardSize(width, height);
                g.playGame();
                if (g.whoWon == Piece.RED) {
                    bWin++;
                } else if (g.whoWon == Piece.BLUE) {
                    aWin++;
                } else {
                    System.out.println("NO WIN");
                }
                reportProgress(game, totalGames);
            } catch (Exception e) {
                System.out.println("Error: " + e.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
        float aPercent = 100 * (float) aWin / (float) totalGames;
        float bPercent = 100 * (float) bWin / (float) totalGames;
        System.out.println();
        if (aWin > bWin) {
            return playerAName + " won " + aPercent + "% of the games played against " + playerBName + "\n" + playerAName + " > " + playerBName + "\n";
        } else if (aWin < bWin) {
            return playerBName + " won " + bPercent + "% of the games played against " + playerAName + "\n" + playerBName + " > " + playerAName + "\n";
        } else {
            return "It was a perfect tie!\n" + playerAName + " = " + playerBName + "\n";
        }
    }

    private static void tournament(int games, int width, int height, String[] names, Class[] playerClasses) {
        if (names.length == playerClasses.length) {

            long startTime = System.currentTimeMillis();

            String results = "";

            for (int p1 = 0; p1 < playerClasses.length - 1; p1++) {
                for (int p2 = p1 + 1; p2 < playerClasses.length; p2++) {
                    try {
                        String p1n = names[p1];
                        String p2n = names[p2];
                        Class p1p = playerClasses[p1];
                        Class p2p = playerClasses[p2];

                        results += singleMatch(games, width, height, p1n, p1p, p2n, p2p);

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getClass().getSimpleName());
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("\n\n -=-=-=-\n" + results);
            long totalTime = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("Tournament completed in " + totalTime + " seconds");
        } else {
            if (names.length < playerClasses.length) {
                System.out.println("Missing name(s)...");
            } else {
                System.out.println("Missing player(s)...");
            }
        }
    }

    public static void main(String[] args) {
        //playHumanVsHuman(8, 8);
        //playHumanVsComputer(5, 5);
        //playComputerVsComputer(11, 5);

        String[] names = {
                "Mike",
                "John",
                "Ryan",
                "Tom",
                "Oli"//,
                //"Frank"
        };
        Class[] classes = {
                ComputerPlayer_mike.class, //Mike
                ComputerPlayer_john.class, //John
                ComputerPlayer_ryan.class, //Ryan
                ComputerPlayer_tom_oli.class, //Tom
                ComputerPlayer_tom_oli.class//, //Oli
                //ComputerPlayer_frank.class  //Frank
        };

        int gamesPerTrial = 500; //Twice this will be played, P1 going first for first trial, P2 for second trial
        int boardWidth = 11;
        int boardHeight = boardWidth;

        tournament(gamesPerTrial, boardWidth, boardHeight, names, classes);

        System.out.println("Tournament used: " + gamesPerTrial + " games each way per matching, and a " + boardWidth + "x" + boardHeight + " board.");
    }
}
