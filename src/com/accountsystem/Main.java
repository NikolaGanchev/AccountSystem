package com.accountsystem;




import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static final Set<Integer> VALIDVALUES = Set.of(1, 2);
    public static void main(String[] args) throws IOException, ParseException, SQLException {
        System.out.println("Enter 1 to create account. Enter 2 to log in.");
        Scanner input = new Scanner(System.in);
            int number = getNumberInput();
            if(!VALIDVALUES.contains(number) && number != 0){
                System.err.println("Please enter either 1 or 2");
            }
            switch (number) {
                case 0:
                    System.err.println("Please enter a number");
                    break;
                case 1:
                    System.out.println("Input your name:");
                    String nameRegister = input.nextLine();
                    System.out.println("Input your password:");
                    String passwordRegister = input.nextLine();
                    System.out.println("Input your email:");
                    String emailRegister = input.nextLine();
                    AccountManager.registerAccountWithEmail(nameRegister, passwordRegister, emailRegister);
                    System.out.println("Account created.");
                    break;
                case 2:

                    try {
                        System.out.println("Input your name:");
                        String nameLogin = input.nextLine();
                        System.out.println("Input your password:");
                        String passwordLogin = input.nextLine();
                        Account account = AccountManager.logIntoAccount(nameLogin, passwordLogin);
                        if(!account.isNull()){
                            System.out.println(account.getUUID());
                            System.out.println("Logged in");
                        }
                        if(account.isNull()){
                            System.out.println("Null account returned");
                        }
                    } catch (ParseException | GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                    ;
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
        return number;
    }
}
