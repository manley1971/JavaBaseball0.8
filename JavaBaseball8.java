/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBaseball;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.Random;

import javafx.scene.media.AudioClip;

/**
 *
 * @author manley
 */
public class JavaBaseball8 extends JApplet {

    public static final double DOUBLE_PLAY_NUMBER = 0.9;

    private static final int JFXPANEL_WIDTH_INT = 600;
    private static final int JFXPANEL_HEIGHT_INT = 250;
    private static JFXPanel fxContainer;
    private BaseballEngine baseballengine;

    public static final class Batter {

        public double homeRunNumber;
        public double tripleNumber;
        public double doubleNumber;
        public double singleNumber;
        public double walkNumber;
        public String batterName;

        Batter(String name, int atBats, int walks, int hits, int doubles, int triples, int homeRuns) {
            batterName = name;
            double pa = atBats + walks;
            homeRunNumber = homeRuns / pa;
            tripleNumber = triples / pa + homeRunNumber;
            doubleNumber = doubles / pa + tripleNumber;
            singleNumber = hits / pa;
            walkNumber = (hits + walks) / pa;

        }

        void PrintStats(Scoreboard scoreboard) {
            System.out.print(". " + batterName);
            System.out.print(" HR:" + homeRunNumber + " 3B:" + tripleNumber);
            System.out.print(" 2B:" + doubleNumber + " 1B:" + singleNumber);
            System.out.println(" BB:" + walkNumber);
            scoreboard.SetBatter(batterName);
        }

    }

    public static final class Lineup {

        private String teamName;
        private Batter[] batters;

        Lineup() {
            System.out.println("  Are you ready for some baseball?  Making a lineup...  ");

            batters = new Batter[9];
        }

        public String GetTeamName() {
            return teamName;
        }

        public void SetBatter(Batter newBatter, int positionInLineup) {
            batters[positionInLineup] = newBatter;
        }

        public Batter GetBatter(int positionInLineup) {
            return batters[positionInLineup];
        }
    }

    public static final class Scoreboard {

        private final int NUMBER_OF_TIMES_TO_PLAY_TUNE_AT_START_OF_GAME = 10;

        private AudioClip happyDing;
        private AudioClip boo;
        private AudioClip hit;
        private AudioClip cheering;
        private AudioClip charge;
        private AudioClip seventhInningStretch;
        private AudioClip playBall;
        private Label batterInfoLabel;
        private Label currentUpdateLabel;
        private Label outsLabel;
        private Label inningLabel;
        private Label homeLabel;
        private Label awayLabel;
        private Label runnersLabel;

        {
            happyDing
                    = new AudioClip(JavaBaseball8.class.getResource("Note1.wav").toString());
            boo
                    = new AudioClip(JavaBaseball8.class.getResource("Boo.wav").toString());
            charge
                    = new AudioClip(JavaBaseball8.class.getResource("Charge.wav").toString());
            hit
                    = new AudioClip(JavaBaseball8.class.getResource("BatHit.wav").toString());
            cheering
                    = new AudioClip(JavaBaseball8.class.getResource("Cheering.wav").toString());
            seventhInningStretch
                    = new AudioClip(JavaBaseball8.class.getResource("TakeMeOut.wav").toString());
            playBall
                    = new AudioClip(JavaBaseball8.class.getResource("PlayBall.wav").toString());
        }

        public void SetBatterInfo(Label batterInfo) {
            batterInfoLabel = batterInfo;
        }

        public void SetBatter(String batterName) {
            if (!batterName.isEmpty()) {
                batterInfoLabel.setText(batterName);
            }
        }

        public void SetCurrentUpdate(Label currentUpdate, Label outs, Label inning, Label home, Label away, Label runners) {
            currentUpdateLabel = currentUpdate;
            outsLabel = outs;
            inningLabel = inning;
            homeLabel = home;
            awayLabel = away;
            runnersLabel = runners;
            currentUpdateLabel.setWrapText(true);
        }

        public void UpdateCurrentInformation(String newInformation) {
            currentUpdateLabel.setText(/*currentUpdateLabel.getText()+*/newInformation);
        }

        public void UpdateScoreboardState(int isHomeTeamAtBat, int inning, int outs, int homeScore, int awayScore, 
                Boolean isRunnerOnFirst, Boolean isRunnerOnSecond, Boolean isRunnerOnThird) {
            String runnerStatusString = "Bases Empty";

            if (isRunnerOnFirst && isRunnerOnSecond && isRunnerOnThird) {
                runnerStatusString = "Bases Loaded";
            }
            if (isRunnerOnFirst && !isRunnerOnSecond && isRunnerOnThird) {
                runnerStatusString = "Runners at the corners";
            }
            if (!isRunnerOnFirst && isRunnerOnSecond && isRunnerOnThird) {
                runnerStatusString = "The only open base is first base";
            }
            if (isRunnerOnFirst && isRunnerOnSecond && !isRunnerOnThird) {
                runnerStatusString = "There is an open base... over at third.";
            }
            if (isRunnerOnFirst && !isRunnerOnSecond && !isRunnerOnThird) {
                runnerStatusString = "Runner on first";
            }
            if (!isRunnerOnFirst && isRunnerOnSecond && !isRunnerOnThird) {
                runnerStatusString = "Runner on second";
            }
            if (!isRunnerOnFirst && !isRunnerOnSecond && isRunnerOnThird) {
                runnerStatusString = "Runners at third";
            }

                inningLabel.setText((isHomeTeamAtBat == 1) ? "Home half of " : "Away half of" + Integer.toString(inning));
                outsLabel.setText(Integer.toString(outs));
                homeLabel.setText(Integer.toString(homeScore));
                awayLabel.setText(Integer.toString(awayScore));
                runnersLabel.setText(runnerStatusString);
            }

        

        

        public void PlayDing() {
            happyDing.play();
        }

        public void PlayBoo() {
            boo.play();
        }

        public void PlayBatHit() {
            hit.play();
        }

        public void PlayCharge() {

            charge.setCycleCount(NUMBER_OF_TIMES_TO_PLAY_TUNE_AT_START_OF_GAME);
            charge.play();
        }

        public void StopCharge() {
            charge.stop();
        }

        public void PlayPlayBall() {
            playBall.play();
        }

        public void PlaySeventhInningStretch() {
            seventhInningStretch.play();
        }

        public void PlayCheering() {
            cheering.play();
        }

    }

    public static final class BaseballEngine {

        private int numberOfOuts = 0;
        private int inning = 1;
        private int[] nowAtBat;
        private int[] homeAndAwayScore;
        private Boolean isRunnerOnFirst = false;
        private Boolean isRunnerOnSecond = false;
        private Boolean isRunnerOnThird = false;
        private Lineup[] homeAndAwayBatters;
        private int isHomeTeamAtBat = 0;
        private Boolean homeTeamIsUp = false;
        private Scoreboard scoreboard;

        public Scoreboard GetScoreboard() {
            return scoreboard;
        }

        public void DrawBaseballEngine() {
            if (isHomeTeamAtBat == 1) {
                System.out.print("Home half of");
            } else {
                System.out.print("Away half of");
            }

            scoreboard.UpdateCurrentInformation(" inning number " + inning + " Home:" + homeAndAwayScore[1] + " Away:" + homeAndAwayScore[0]);
            scoreboard.UpdateScoreboardState(isHomeTeamAtBat, inning, numberOfOuts, homeAndAwayScore[1], homeAndAwayScore[0], 
                    isRunnerOnFirst, isRunnerOnSecond, isRunnerOnThird);
            System.out.println(" inning number " + inning + "  Home:" + homeAndAwayScore[1] + " Away:" + homeAndAwayScore[0]);
        }

        public Lineup Make82Angels(String name) {
            Lineup lineup = new Lineup();
            lineup.teamName = name;
            lineup.batters[0] = new Batter("The Kangaroo", 616, 69, 239, 38, 16, 14);
            lineup.batters[1] = new Batter("Dewey", 541, 106, 165, 37, 2, 34);
            lineup.batters[2] = new Batter("Lynn", 531, 82, 177, 42, 1, 39);
            lineup.batters[3] = new Batter("Mr October", 541, 114, 151, 36, 3, 47);
            lineup.batters[4] = new Batter("Pops", 522, 80, 156, 43, 3, 44);
            lineup.batters[5] = new Batter("Grich", 352, 40, 107, 14, 2, 22);
            lineup.batters[6] = new Batter("Baylor", 628, 71, 186, 33, 3, 36);
            lineup.batters[7] = new Batter("Dougie D", 575, 66, 173, 42, 5, 30);
            lineup.batters[8] = new Batter("Downing", 509, 77, 166, 27, 3, 12);
            return lineup;

        }

        public Lineup Make82RedSox(String name) {
            Lineup lineup = new Lineup();
            lineup.teamName = name;
            lineup.batters[0] = new Batter("Brett", 449, 58, 175, 33, 9, 24);
            lineup.batters[1] = new Batter("big dog", 587, 83, 186, 28, 6, 40);
            lineup.batters[2] = new Batter("Scmidt", 354, 73, 112, 19, 2, 31);
            lineup.batters[3] = new Batter("Yaz", 566, 128, 186, 29, 0, 40);
            lineup.batters[4] = new Batter("Rice", 677, 58, 213, 25, 15, 46);
            lineup.batters[5] = new Batter("Miller", 427, 50, 125, 15, 5, 2);
            lineup.batters[6] = new Batter("Remy", 410, 36, 110, 9, 1, 0);
            lineup.batters[7] = new Batter("Hoffman", 279, 25, 77, 17, 2, 6);
            lineup.batters[8] = new Batter("G Allenson", 714, 94, 159, 35, 2, 14);
            return lineup;

        }

        public BaseballEngine() {
            scoreboard = new Scoreboard();

            nowAtBat = new int[]{1, 1};
            homeAndAwayScore = new int[]{0, 0};
            homeAndAwayBatters = new Lineup[2];
            homeAndAwayBatters[1] = Make82Angels("Wordsworth Wonders");
            homeAndAwayBatters[0] = Make82RedSox("Liam's Poo Team");

        }

        public void HomeRun() {
            scoreboard.PlayDing();
            scoreboard.PlayCheering();

            homeAndAwayScore[isHomeTeamAtBat]++;
            if (isRunnerOnFirst) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            if (isRunnerOnSecond) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            if (isRunnerOnThird) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            isRunnerOnFirst = false;
            isRunnerOnSecond = false;
            isRunnerOnThird = false;
            System.out.println("It's a dinger!");

        }

        public void Triple() {
            scoreboard.PlayBatHit();
            if (isRunnerOnFirst) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            if (isRunnerOnSecond) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            if (isRunnerOnThird) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            isRunnerOnFirst = false;
            isRunnerOnSecond = false;
            isRunnerOnThird = true;

            System.out.println("It's a triple!");
        }

        public void Double() {
            scoreboard.PlayCheering();
            if (isRunnerOnSecond) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            if (isRunnerOnThird) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            isRunnerOnSecond = true;
            isRunnerOnThird = isRunnerOnFirst;
            isRunnerOnFirst = false;

            System.out.println("It's a double!");
        }

        public void Single() {
            if (isRunnerOnThird) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            isRunnerOnSecond = isRunnerOnFirst;
            isRunnerOnThird = isRunnerOnSecond;
            isRunnerOnFirst = true;

            System.out.println("It's a single!");
        }

        public void BaseOnBalls() {
            if (isRunnerOnThird && isRunnerOnSecond && isRunnerOnFirst) {
                homeAndAwayScore[isHomeTeamAtBat]++;
            }
            isRunnerOnThird = (isRunnerOnSecond && isRunnerOnFirst || (isRunnerOnThird && !isRunnerOnSecond) || (isRunnerOnThird && !isRunnerOnFirst));
            isRunnerOnSecond = isRunnerOnFirst || (isRunnerOnSecond && !isRunnerOnFirst);
            isRunnerOnFirst = true;
            System.out.println("It's a walk!");
        }

        public void PrintNextBatter() {
            int placeInLineup = nowAtBat[isHomeTeamAtBat];
            Lineup lineup = homeAndAwayBatters[isHomeTeamAtBat];
            Batter batterAtBat = lineup.batters[placeInLineup - 1];
            System.out.print("Up next  ");
            System.out.print("Batter #" + placeInLineup);

            batterAtBat.PrintStats(scoreboard);
        }

        public void PointToNextBatterInLineup() {
            nowAtBat[isHomeTeamAtBat]++;
            if (nowAtBat[isHomeTeamAtBat] == 10) {
                nowAtBat[isHomeTeamAtBat] = 1;
            }
        }

        public void HandleBatter(double randomNumber) {
            //Liam's second hack
            //if (isHomeTeamAtBat==1) {
            //randomNumber =.95;
            //     }

            int placeInLineup = nowAtBat[isHomeTeamAtBat];
            Lineup lineup = homeAndAwayBatters[isHomeTeamAtBat];
            Batter batterAtBat = lineup.batters[placeInLineup - 1];

            System.out.println("The batter rolls a random " + randomNumber);

            if (randomNumber < batterAtBat.homeRunNumber) {
                HomeRun();
            } else if (randomNumber < batterAtBat.tripleNumber) {
                Triple();
            } else if (randomNumber < batterAtBat.doubleNumber) {
                Double();
            } else if (randomNumber < batterAtBat.singleNumber) {
                Single();
            } else if (randomNumber < batterAtBat.walkNumber) {
                BaseOnBalls();
            } else if (randomNumber < DOUBLE_PLAY_NUMBER) {
                System.out.println("It's a regular out...");
                numberOfOuts++;
            } else {
                System.out.print("Double play ball grounded to the hole... ");
                if (isRunnerOnFirst && (numberOfOuts < 2)) {
                    scoreboard.PlayBoo();
                    scoreboard.PlayBoo();
                    System.out.println("with the runner at first being retired on his way to second!");
                    numberOfOuts = numberOfOuts + 2;
                    isRunnerOnFirst = false;
                } else {
                    System.out.println("but there is only one out recorded.  Lucky dog!");
                    scoreboard.PlayBoo();
                    scoreboard.PlayBoo();
                    numberOfOuts = numberOfOuts + 1;
                }
            }

            PointToNextBatterInLineup();

            if (numberOfOuts >= 3) {
                System.out.println("Inning is over");
                scoreboard.PlayPlayBall();
                if (inning == 7 && (isHomeTeamAtBat == 0)) {
                    scoreboard.PlaySeventhInningStretch();
                }

                isRunnerOnFirst = false;
                isRunnerOnSecond = false;
                isRunnerOnThird = false;
                numberOfOuts = 0;

                if (isHomeTeamAtBat == 1) {
                    if (inning == 9) {
                        System.out.println("Regulation 9 innings is over, computers don't get sleepy though so if you keep clicking I'll keep calculating!");
                    }
                    inning++;
                    isHomeTeamAtBat = 0;
                } else {
                    isHomeTeamAtBat = 1;
                }
                DrawBaseballEngine();
            }
            scoreboard.UpdateScoreboardState(isHomeTeamAtBat, inning, numberOfOuts, homeAndAwayScore[1], homeAndAwayScore[0],
                    isRunnerOnFirst, isRunnerOnSecond, isRunnerOnThird);
        }

    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }

                JFrame frame = new JFrame("Java Baseball Scorebaord 8");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JApplet applet = new JavaBaseball8();
                applet.init();

                frame.setContentPane(applet.getContentPane());

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                applet.start();
            }
        });
    }

    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));

        add(fxContainer, BorderLayout.PAGE_START);
        // create JavaFX scene
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                createScene();
            }
        });
    }

    private void createScene() {
        BaseballEngine baseballEngine = new BaseballEngine();
        baseballEngine.GetScoreboard().PlayCharge();

        Random rand = new Random();

        Button batterUp = new Button();
        Button walkBatter = new Button();
        walkBatter.setText("Walk him");

        Button stealABase = new Button();
        stealABase.setText("Steal");

        Label fixed1 = new Label("Current Batter: ");
        Label fixed2 = new Label("Outs");
        Label fixed3 = new Label("Inning");
        Label fixed4 = new Label("Home");
        Label fixed5 = new Label("Away");
        Label fixed6 = new Label("Runners:");
        Label outs = new Label("0");
        Label inning = new Label("1");
        Label home = new Label("0");
        Label away = new Label("0");
        Label runners = new Label("bases empty");

        Label batterInfo = new Label(" **fill in with info **");
        Label currentUpdate = new Label("Let's play two!");

        baseballEngine.GetScoreboard().SetBatterInfo(batterInfo);
        baseballEngine.GetScoreboard().SetCurrentUpdate(currentUpdate, outs, inning, home, away, runners);
        baseballEngine.PrintNextBatter();

        batterUp.setText("Pitch to him!  Don't be a chicken!");
        batterUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                baseballEngine.GetScoreboard().StopCharge();
                baseballEngine.GetScoreboard().PlayBatHit();

                // to run simulations uncomment this and run it for a bunch of innings
                //    for (int i = 0; i < 100000; i++) 
                baseballEngine.HandleBatter(rand.nextDouble());
                baseballEngine.PrintNextBatter();

            }

        });

        walkBatter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                baseballEngine.GetScoreboard().StopCharge();
                for (int i = 0; i < 100; i++) {
                    baseballEngine.GetScoreboard().PlayBoo();
                }

                baseballEngine.BaseOnBalls();
                baseballEngine.PointToNextBatterInLineup();
                baseballEngine.DrawBaseballEngine();
                baseballEngine.PrintNextBatter();

            }

        });

        GridPane root = new GridPane();
        root.add(walkBatter, 1, 0);
        root.add(batterUp, 0, 0);
        root.add(stealABase, 2, 0);
        root.add(fixed1, 0, 1);
        root.add(batterInfo, 1, 1);
        root.add(currentUpdate, 1, 3);
        root.add(fixed2, 0, 4);
        root.add(fixed3, 0, 5);
        root.add(fixed4, 0, 6);
        root.add(fixed5, 0, 7);
        root.add(fixed6, 0, 8);

        root.add(outs, 1, 4);
        root.add(inning, 1, 5);
        root.add(home, 1, 6);
        root.add(away, 1, 7);
        root.add(runners, 1, 8);
        fxContainer.setScene(new Scene(root));
    }

}
