import java.util.Scanner;
public class NumberGame {

    public static int play(int target){
        Scanner sc= new Scanner(System.in);
         int points=0;
         System.out.println("WELCOME TO THE NUMBER GUESSING GAME!");
         System.out.println("GUESS THE NUMBER BETWEEN 1 TO 100");
         int guess=sc.nextInt();
        
         if(guess==target){
            System.out.println("YOUR GUESS IS CORRECT 1 POINT OFFERED!");
            points= points+1;
         }

         while(guess!=target){

            if(guess==target){
            points++;
            System.out.println("Correct! You have earned a point.");
            break;
         }

            if(guess<=(target-10)){
            System.err.println("too low guess!");
          }

        else if(guess>=(target+10)){
            System.err.println("too high guess!");
        }

        else if(guess>=target-1 && guess<=target+1){
            System.err.println("You are very close!");
        }

        else if(guess>=target-10 && guess<=target+10){
            System.err.println("You are close!");
        }

        

        
             System.err.println("Incorrect! Try again.");
             guess=sc.nextInt();

         }

          
         

        return points;
    }

    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.println("Welcome to the Number Game!");
         int points=0;
         int choice=0;

         
    
          while(true){
              System.err.println("ENTER 1 TO PLAY OR 0 TO EXIT");
              choice=x.nextInt();

               if(choice==0){
              System.err.println("Thank you for playing!");
              break;
              }

              int target=(int) (Math.random()*100+1);
              System.out.println(target);
              points+=play(target);
        }

        System.err.println("TOTAL POINTS SCORED :"+points);

         
    }
}