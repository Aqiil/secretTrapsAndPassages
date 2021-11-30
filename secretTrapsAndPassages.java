/******************************************
    @author    Abubakar Aqiil
    @date      22 Nov 2021
    @version   1
    
    A program that simulates a game of secret traps and passages.
******************************************/

import java.util.Scanner; // Needed to make Scanner available
import java.util.Random; // Needed to make Random available
import java.util.concurrent.TimeUnit; // Needed to make TimeUnit available

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
    
    // Creates a new player object
    // 
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
        try {
          TimeUnit.SECONDS.sleep(secondsToSleep);
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
    } // END delayCode
    
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
            System.out.print("Enter player " + i + " name: ");
            new_player_name = scanner.nextLine();
            player new_player = createPlayer(new_player_name, 1, 0);
            players[i-1] = new_player;
        }

        return players;
    } // END newPlayers
    
    // Simulates a game of secret traps and passages
    // 
    public static void playGame()
    {
        int[] board = {37, 0, 0, 10, 0, 0, 0, 22, 0, 0,
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  21, 0, 0, 0, 0, 0, 0, 48, 0, 0,
                  0, 0, 0, 0, 0, -30, 0, 0, 0, 0,
                  0, 0, 0, 0, 0, 0, 0, -22, 0, 17,
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  21, 0, 0, 0, 0, 0, 0, 0, 0, 19,
                  0, 0, 0, 0, 0, 0, 0, 64, 0, 0,
                  0, 0, 0, 0, 39, 0, 19, 0, 0, 0};

        //System.out.println("The board has " + board.length + " squares."); // Check how many squares board has

        player[] players = newPlayers();
        boolean endOfBoard = false;

        while (!endOfBoard)
        {
            // Give each player a turn
            for (int i = 1; i < players.length + 1; i++)
            {
                endOfBoard = playerTurn(board, players[i-1]);

                // Checks if the player has won the game
                if (endOfBoard)
                {
                    System.out.println(getName(players[i-1]) + " has won the game in " + getTurns(players[i-1]) + " turns! Congratulations!");
                    return;
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
    } // END playGame
    
    
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
