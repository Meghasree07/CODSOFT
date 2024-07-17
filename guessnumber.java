package javatask.java;
import java.util.Random;
import java.util.Scanner;

public class task1{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int maxAttempts = 5; 
        boolean playAgain = true;

        while (playAgain) {
            
            int randomNumber = random.nextInt(100) + 1;
            int attempts = 0;
            boolean guessedCorrectly = false;
            while (!guessedCorrectly && attempts < maxAttempts) {
                System.out.println("Guess a number between 1 and 100 (inclusive): ");
                int guess = scanner.nextInt();

                attempts++;

                if (guess == randomNumber) {
                    guessedCorrectly = true;
                    System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");
                } else if (guess < randomNumber) {
                    System.out.println("Your guess is too low. Try again!");
                } else {
                    System.out.println("Your guess is too high. Try again!");
                }
            }

            if (!guessedCorrectly) {
                System.out.println("You ran out of attempts. The number was " + randomNumber + ".");
            }
            System.out.println("Do you want to play again? (yes/no)");
            playAgain = scanner.next().equalsIgnoreCase("yes");
        }

        scanner.close();
    }
}
