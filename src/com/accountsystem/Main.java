package com.accountsystem;




import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static final Set<Integer> VALIDVALUES = Set.of(1, 2);
    public static void main(String[] args) throws IOException {
        System.out.println("Enter 1 to create account. Enter 2 to log in.");

            int number = getNumberInput();
            if(!VALIDVALUES.contains(number) && number != 0){
                System.err.println("Please enter either 1 or 2");
            }
            switch (number) {
                case 0:
                    System.err.println("Please enter a number");
                    break;
                case 1:
                    AccountManager.registerAccount("Account", "Account");
                    System.out.println("Account created.");
                    break;
                case 2:
                    //Todo AccountManager.logIn();
                    System.out.println("Logged in");
                    break;
                default:

            }
        }


    static int getNumberInput(){
        Scanner input = new Scanner(System.in);
        int number = 0;
        try {
            number = input.nextInt();
        }
        catch (Exception e){
            input.close();
            return 0;
        }
        input.close();
        return number;
    }
}
