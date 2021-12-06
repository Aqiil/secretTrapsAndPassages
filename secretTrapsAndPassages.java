/******************************************
    @author    Abubakar Aqiil
    @date      22 Nov 2021
    @version   1
    
    A program that simulates a game of secret traps and passages.
******************************************/

import java.util.Scanner; // Needed to make Scanner available
import java.util.Random; // Needed to make Random available
import java.util.concurrent.TimeUnit; // Needed to make TimeUnit available
import java.util.ArrayList; // import the ArrayList class
import java.io.*; // Needed for file input/output operations

class quizQuestion
{
    String question;
    String choice1;
    String choice2;
    String choice3;
    String choice4;
    String answer;
}

class player
{
    String name;
    int position;
    int turns;
}


class secretTrapsAndPassages
{
    public static void main (String [] a)
    {
        playGame();
        System.exit(0);
    } // END main

    // Creates a new quizQuestion object
    // 
    public static quizQuestion createQuizQuestion(String question, String choice1, String choice2, String choice3, String choice4, String answer)
    {
        quizQuestion q = new quizQuestion();
        
        setQuestion(q, question);
        setChoice1(q, choice1);
        setChoice2(q, choice2);
        setChoice3(q, choice3);
        setChoice4(q, choice4);
        setAnswer(q, answer);
        
        return q;
    } // END createQuizQuestion

    // Asks a question and reads in an answer
    // 
    public static String askQuestion(quizQuestion q)
    {
        String response;
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(getQuestion(q));
        System.out.println("A. " + getChoice1(q));
        System.out.println("B. " + getChoice2(q));
        System.out.println("C. " + getChoice3(q));
        System.out.println("D. " + getChoice4(q));

        response = scanner.nextLine().trim(); // trim() method removes whitespace
        return response;
    } // END askQuestion

    // Checks if the user answered a question correctly
    // 
    public static boolean checkAnswer(quizQuestion q, String answer)
    {
        if (getAnswer(q).equals(answer))
        {
            System.out.println("Well done! You answered correctly!\n");
            return true;
        }
        else
        {
            System.out.println("Oh no! Better luck next time.\n");
            return false;
        }
    } // END checkAnswer

    // Simulates a dice roll
    // 
    public static int rollDice()
    {
        Random rand = new Random();
        int upperbound = 6; // Generates values from 0 to 5
        
        int roll = rand.nextInt(upperbound) + 1; // Adjusts range to 1 to 6
        return roll;
    } // END rollDice

    public static player createPlayer(String name, int position, int turns)
    {
        player p = new player();
        
        setName(p, name);
        setPosition(p, position);
        setTurns(p, turns);
        
        return p;
    } // END createPlayer

    // Check if player has reached end of board
    // 
    public static boolean checkEndBoard(int[] board, player p)
    {
        if (getPosition(p) >= board.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    } // END checkEndBoard

    // Delay the execution of a program
    // 
    public static void delayCode(int secondsToSleep)
    {
        try 
        {
        TimeUnit.SECONDS.sleep(secondsToSleep);
        } catch (InterruptedException ie) 
        {
        ie.printStackTrace();
        }
    } // delayCode

    // Simulates a players turn
    // 
    public static boolean playerTurn(int[] board, player p)
    {
        System.out.println("\n### " + getName(p).toUpperCase() + "'S TURN ###");
        delayCode(1);
        System.out.println("[+] Rolling dice...");
        delayCode(2);
        int roll = rollDice();
        System.out.println(getName(p) + " rolled " + roll + ".\n");
        
        int current_position = getPosition(p) + roll;
        setPosition(p, current_position);
        setTurns(p, getTurns(p) + 1);
        
        boolean endOfBoard = checkEndBoard(board, p);
        if (endOfBoard)
        {
            return endOfBoard;
        }
        
        int board_square = board[current_position];
        quizQuestion question = createQuizQuestion("What won the EURO 2020 cup?", "Italy", "England", "Germany", "Brazil", "Italy"); 
        
        if (board_square > 0)
        {
            System.out.println("You've landed on a ladder!\n");
        }
        else if (board_square < 0)
        {
            System.out.println("You've landed on a trap!\n");
        }
        else
        {
            System.out.println("You've landed on a normal square!\n");
            delayCode(1);
            return endOfBoard;
        }
        
        delayCode(1);
        String answer = askQuestion(question);
        boolean correct_answer = checkAnswer(question, answer);
        delayCode(1);
        
        if (correct_answer && board_square > 0)
        {
            current_position = current_position + board_square;
            System.out.println("You've climbed up the ladder.\n");
            setPosition(p, current_position);
        }
        else if (!(correct_answer) && board_square < 0)
        {
            current_position = current_position + board_square; // Adding a negative number
            System.out.println("You've been eaten by a snake! Better luck next time.\n");
            setPosition(p, current_position);
        }
        else
        {
            return endOfBoard;
        }
        
        // Second check is needed to see if the user has won after going up a ladder
        endOfBoard = checkEndBoard(board, p);
        if (endOfBoard)
        {
            return endOfBoard;
        }
        
        return endOfBoard;
    } // END playerTurn

    // Initialize new players for a new game
    // 
    public static player[] newPlayers()
    {
        int player_count;
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("How many players are there? ");
        player_count = Integer.parseInt(scanner.nextLine()); // Changed code using nextInt() before nextLine() as this causes scanner to read in \n instead of next line as nextInt() keeps cursor on same line.
        
        player[] players = new player[player_count];
        String new_player_name;
        
        for (int i = 1; i < player_count + 1; i++)
        {
            System.out.println("Enter player " + i + " name: ");
            new_player_name = scanner.nextLine();
            player new_player = createPlayer(new_player_name, 1, 0);
            players[i-1] = new_player;
        }
        
        return players;
    } // END newPlayers

    // Export the leadboard
    // 
    public static void exportLeaderboard(player[] players) throws IOException
    { 
        PrintWriter outputStream = new PrintWriter(new FileWriter("leaderboard.csv"));
        outputStream.println("Name, Position, Turns");
        
        for (player p : players) 
        { 
            outputStream.println(getName(p) + ", " + getPosition(p) + ", " + getTurns(p));
        }
        
        outputStream.close();
    } // END exportLeaderboard

    // Export the current game
    // 
    public static void exportGame(player[] players) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Please enter the csv filename (without file extension): ");
        String filename = scanner.nextLine();
        
        PrintWriter outputStream = new PrintWriter(new FileWriter(filename + ".csv"));
        outputStream.println("Name, Position, Turns");
        
        for (player p : players) 
        { 
            outputStream.println(getName(p) + ", " + getPosition(p) + ", " + getTurns(p));
        }
        outputStream.close();
    } // END exportGame

    // Import leaderboard
    // 
    public static player[] importLeaderboard() throws IOException
    {
        BufferedReader inputStream = new BufferedReader(new FileReader("leaderboard.csv"));
        int index = 0;
        
        ArrayList<player> players = new ArrayList<player>(); // Create an ArrayList object
        
        String line;
        inputStream.readLine();
        while ((line = inputStream.readLine()) != null)
        {
            String[] values = line.split(", ");
            player p = createPlayer(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]));
            players.add(p);
        }
        
        inputStream.close();
        
        player[] playersArray = players.toArray(new player[players.size()]);
        return playersArray;
    } // END importLeaderboard

    // Import existing board
    // 
    public static player[] importGame() throws IOException
    {
        String filename = "";
        Scanner scanner = new Scanner(System.in);
        
        boolean fileExists = false;
        
        while (!fileExists)
        {
            System.out.println("Please enter the csv filename (without file extension): ");
            filename = scanner.nextLine();
            
            // Get file by creating new File object
            File f = new File(filename + ".csv");

            if (f.exists())
            {
                //System.out.println("DEBUG: FILE EXISTS\n");
                fileExists = true;
            }
            else
            {
                System.out.println("There was an error opening your file. Please try again.\n");
            }
        }

        BufferedReader inputStream = new BufferedReader(new FileReader(filename + ".csv"));
        int index = 0;
        
        ArrayList<player> players = new ArrayList<player>(); // Create an ArrayList object
        
        String line;
        inputStream.readLine();
        while ((line = inputStream.readLine()) != null)
        {
            String[] values = line.split(", ");
            player p = createPlayer(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]));
            players.add(p);
        }
        
        inputStream.close();
        
        player[] playersArray = players.toArray(new player[players.size()]);
        return playersArray;
    } // END importGame

    // Check if user would like to load an existing game or create a new game
    // 
    public static player[] gameMode() 
    {
        int response = -1;
        Scanner scanner = new Scanner(System.in);
        
        player[] players;
        boolean loop = true;
        
        while (loop)
        {
            while (!(response == 1 || response == 2))
            {
                System.out.println("Choose an option:\n"
                                + "1. Start new game\n"
                                + "2. Load existing game");
                System.out.print("Choice (1 or 2): ");
                response = scanner.nextInt();
                scanner.nextLine();
            }
            
            if (response == 2)
            {
                try
                {
                    players = importGame();
                    return players;
                }
                catch (IOException e)
                { 
                }
            }
            else
            {
                players = newPlayers();
                return players;
            }
        }
        
        System.out.println("DEBUG: Returning players array");
        
        players = new player[0];
        return players;
    } // END gameMode

    // Swap two entires in an array of type player
    // 
    public static void swap(player[] leaderboard, int currentPosition, int nextPosition)
    {
        /*
        player lowerValue = leaderboard[currentPosition];
        player higherValue = leaderboard[nextPosition];
        
        leaderboard[currentPosition] = lowerValue;
        leaderboard[nextPosition] = higherValue;
        */
        
        player temp = leaderboard[currentPosition];
        leaderboard[currentPosition] = leaderboard[nextPosition];
        leaderboard[nextPosition] = temp;
    } // END swap

    // Sort the leaderboard by lowest turn count
    // 
    public static void sortLeaderboard(player[] leaderboard)
    {
        final int ARRAY_SIZE = leaderboard.length;
        //System.out.println(ARRAY_SIZE);
        
        boolean sorted = false;
        int pass = 0;
        
        while (!sorted)
        {
            sorted = true;
            
            for (int position = 0; position < ARRAY_SIZE - 1 - pass; position++) // Subtract 1 as array is indexed from 0
            {
                //System.out.println("DEBUG: Checking " + position + " < " + (ARRAY_SIZE - pass - 1));
                //System.out.println("DEBUG:          " + getTurns(leaderboard[position]) + " < " + getTurns(leaderboard[position + 1]));
                if (getTurns(leaderboard[position]) > getTurns(leaderboard[position + 1]))
                {
                    //System.out.println("DEBUG: Swapping");
                    swap(leaderboard, position, position + 1);
                    //System.out.println("DEBUG: SWAPPED");
                    sorted = false;
                }
            }
            
            //System.out.println("DEBUG: Increment Pass\n");
            pass = pass + 1;
        }
    } // END sortLeaderboard

    // Update the leaderboard with a new winner
    // 
    public static player[] updateLeaderboard(player[] players, player winner)
    {
        int size = players.length;
        
        // Creates temp array of type player
        player[] temp = new player[size + 1];
        
        // System.out.println("DEBUG: Copying values");
        // Copy across existing values
        for (int i = 0; i < size; i++)
        {
            temp[i] = players[i];
        }
        
        // System.out.println("DEBUG: Adding new value");
        // Adds in new value
        temp[size] = winner;
        players = temp;
        
        // System.out.println("DEBUG: Sorting values");
        sortLeaderboard(players);
        
        return players;
    } // END updateLeaderboard

    // Print the leaderboard to the user
    // 
    public static void printLeaderboard(player winner)
    {
        try
        {
            player[] pastWinners = importLeaderboard();
            player[] currentWinners = updateLeaderboard(pastWinners, winner);
            exportLeaderboard(currentWinners);
            
            System.out.println("*** LEADERBOARD ***");
            for (int i = 1; i <= currentWinners.length; i++)
            {
                System.out.println("[" + i + "] " + getName(currentWinners[i-1]) + ", Turns: " + getTurns(currentWinners[i-1]));
            }
        }
        catch (IOException e)
        {
        }
    } // END printLeaderboard
    
    // Simulates a game of secret traps and passages
    // 
    public static void playGame()
    {
        int[] board = {0, 0, 37, 10, 0, 0, 0, 22, 0, 0,
                  0, 0, 0, 15, 0, 0, 0, 0, 0, 0,
                  21, 0, 0, 0, 0, 0, 0, 48, 0, 0,
                  0, 0, 0, 0, 0, -30, 0, 0, 0, 0,
                  0, 0, 0, 0, 0, 0, 0, -22, 0, 17,
                  0, 0, 13, 0, 0, 0, 0, 0, 20, 0,
                  0, 0, 0, -20, 0, 6, 0, 0, 0, 0,
                  21, 0, 0, 0, -10, 0, 0, 0, 0, 19,
                  0, 0, 0, 0, 0, 0, 0, -64, 0, 0,
                  0, 0, 0, 0, -39, 0, -19, 0, 0, 0};

        //System.out.println("The board has " + board.length + " squares."); // Check how many squares board has

        player[] players = gameMode(); // Ask to load a game or play new game
        //player[] players = newPlayers(); // Comment out
        boolean endOfBoard = false;
        player winner;

        while (!endOfBoard)
        {
            // Give each player a turn
            for (int i = 1; i < players.length + 1; i++)
            {
                endOfBoard = playerTurn(board, players[i-1]);

                // Checks if the player has won the game
                if (endOfBoard)
                {
                    winner = players[i-1];
                    System.out.println(getName(winner) + " has won the game in " + getTurns(winner) + " turns! Congratulations!\n");
                    printLeaderboard(winner);
                    System.out.println("DEBUG: END OF GAME");
                    return; // EXIT loop instead of return
                }
            }

            // Print positions of each player if there is more than one player
            if (players.length > 1)
            {
                delayCode(1);
                // Prints out the current positions of each player
                // USE sorting algorithm to create function where player in the lead is sorted out if 
                System.out.println("### BOARD POSITIONS ###");
                for (int i = 1; i < players.length + 1; i++)
                {
                    System.out.println("[-] " + getName(players[i-1]) + " is at board position " + getPosition(players[i-1]) + ".");
                }
                delayCode(1);
            }
        }
    }
    
    
    // GETTER AND SETTER METHODS // 
    
    
    // Getter method to get the question
    public static String getQuestion(quizQuestion q)
    {
        return q.question;
    }

    // Getter method to get choice 1
    public static String getChoice1(quizQuestion q)
    {
        return q.choice1;
    }

    // Getter method to get choice 2
    public static String getChoice2(quizQuestion q)
    {
        return q.choice2;
    }

    // Getter method to get choice 3
    public static String getChoice3(quizQuestion q)
    {
        return q.choice3;
    }

    // Getter method to get choice 4
    public static String getChoice4(quizQuestion q)
    {
        return q.choice4;
    }

    // Getter method to get the answer
    public static String getAnswer(quizQuestion q)
    {
        return q.answer;
    }

    // Setter method to set value of question
    public static void setQuestion(quizQuestion q, String question)
    {
        q.question = question;
    }

    // Setter method to set value of choice 1 for question
    public static void setChoice1(quizQuestion q, String value)
    {
        q.choice1 = value;
    }

    // Setter method to set value of choice 2 for question
    public static void setChoice2(quizQuestion q, String value)
    {
        q.choice2 = value;
    }

    // Setter method to set value of choice 3 for question
    public static void setChoice3(quizQuestion q, String value)
    {
        q.choice3 = value;
    }

    // Setter method to set value of choice 4 for question
    public static void setChoice4(quizQuestion q, String value)
    {
        q.choice4 = value;
    }

    // Setter method to set answer to question
    public static void setAnswer(quizQuestion q, String answer)
    {
        q.answer = answer;
    }

    // Getter method to retrieve player name
    public static String getName(player p)
    {
        return p.name;
    }

    // Getter method to retrieve player name
    public static int getPosition(player p)
    {
        return p.position;
    }

    // Getter method to retrieve how many turns player has had
    public static int getTurns(player p)
    {
        return p.turns;
    }

    // Setter method to set player name
    public static void setName(player p, String name)
    {
        p.name = name;
    }

    // Setter method to set player position
    public static void setPosition(player p, int position)
    {
        p.position = position;
    }

    // Setter method to set how many turns player has had
    public static void setTurns(player p, int turns)
    {
        p.turns = turns;
    }

}
