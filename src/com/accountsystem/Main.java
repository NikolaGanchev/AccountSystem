package com.accountsystem;




import com.sun.jdi.InvalidTypeException;

import javax.security.auth.login.AccountException;
import java.lang.constant.Constable;
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


    static Integer getNumberInput(){
        Scanner input = new Scanner(System.in);
        int number = 0;
        try {
            number = input.nextInt();
        }
        catch (Exception e) {
            return null;
        }
        return number;
    }

    static void doStuff() throws AccountException, SQLException, InvalidTypeException{
        System.out.println("Enter 1 to create account. Enter 2 to log in.");
        Scanner input = new Scanner(System.in);
        int number = getNumberInput();
        if(!VALIDVALUES.contains(number)){
            System.err.println("Please enter either 1 or 2");
            return;
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
                try {
                    AccountManager.registerAccountWithEmail(nameRegister, passwordRegister, emailRegister);
                } catch (AccountEmailAlreadyExistsException e) {
                    e.printStackTrace();
                }
                System.out.println("Account created.");
                break;
            case 2:

                    System.out.println("Input your email:");
                    String emailLogin = input.nextLine();
                    System.out.println("Input your password:");
                    String passwordLogin = input.nextLine();
                    try {
                        Account account = AccountManager.logIntoAccountWithEmail(emailLogin, passwordLogin);
                        System.out.println(account.getUUID());
                        System.out.println(account.getEmail());
                        System.out.println("Logged in");
                        System.out.println("If you want to delete account, enter 3. \n If you want to change name, enter 4. \n Email is 5. \n Password is 6.");
                        switch (input.nextLine()) {
                            case("3"):
                                AccountManager.deleteAccount(account);
                                System.out.println("Account deleted");
                                break;
                            case("4"):
                                System.out.println("Enter new name:");
                                String newName = input.nextLine();
                                account.changeName(newName);
                                break;
                            case("5"):
                                System.out.println("Enter new email:");
                                String newEmail = input.nextLine();
                                account.changeEmail(newEmail);
                                break;
                            case("6"):
                                System.out.println("Enter new password:");
                                String newPassword = input.nextLine();
                                account.changePassword(newPassword);
                                break;
                            default:
                                break;
                        }
                    }
                    catch (NoMatchingAccountException e){
                        System.out.println("Null account returned");
                    } catch (AccountEmailAlreadyExistsException e) {
                        e.printStackTrace();
                    }

                break;
            default:

        }
    }
}
