import java.util.Scanner;

public class GradeCalculator {
    public static void main(String[] args) {
        Scanner x= new Scanner(System.in);

        // Input: Number of subjects
        System.out.print("Enter the number of subjects:");
        int n= x.nextInt();

        int[] marks =new int[n];
        int total=0;

        // Input: Marks for each subject
        for (int i=0; i<n; i++) {
            System.out.print("Enter marks obtained in subject "+(i+1)+"."+"(out of 100):");
            marks[i] = x.nextInt();
            total+= marks[i];
        }

        // Calculate total marks and average percentage
        double average =(double)(total/n);

        // Grade calculation as per average percentage
        String grade;
        if (average>=90) {
            grade="A+";
        } 
        else if(average>=80) {
            grade="A";
        } 
        else if(average>=70) {
            grade="B";
        } 
        else if(average>=60) {
            grade = "C";
        }
         else if(average>=50) {
            grade="D";
        } 
        else {
            grade="F";
        }

        // Display results
        System.out.println("\n=========RESULT==========");
        System.out.println("Total Marks: "+ total +"out of "+(n*100));
        System.out.printf("Average Percentage: %.2f%%\n",average);
        System.out.println("Grade:"+grade);
    }
}
