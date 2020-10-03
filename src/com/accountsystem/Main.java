package com.accountsystem;




import com.sun.jdi.InvalidTypeException;

import javax.security.auth.login.AccountException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static final Set<Integer> VALIDVALUES = Set.of(1, 2);
    public static void main(String[] args) throws SQLException, InvalidTypeException, AccountException {
        while(true){
            doStuff();
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

    static void doStuff() throws AccountException, SQLException, InvalidTypeException{
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
                    System.out.println("Input your email:");
                    String emailLogin = input.nextLine();
                    System.out.println("Input your password:");
                    String passwordLogin = input.nextLine();
                    Account account = AccountManager.logIntoAccountWithEmail(emailLogin, passwordLogin);
                    if(!account.isNull()){
                        System.out.println(account.getUUID());
                        System.out.println(account.getEmail());
                        System.out.println("Logged in");
                        System.out.println("If you want to delete account, enter 3");
                        if(input.nextLine().equals("3")){
                            AccountManager.deleteAccount(account);
                            System.out.println("Account deleted");
                        }
                    }
                    if(account.isNull()){
                        System.out.println("Null account returned");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
                break;
            default:

        }
    }
}
