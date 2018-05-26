import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

// Class of text user interface
public class TUI {
    private Scanner input = new Scanner(System.in);

    //Starting menu
    public void start(){
        showMenu();
        System.out.print("\nĮveskite meniu pasirinkimą: ");
        int choice = input.nextInt();
        input.nextLine();
        while(true){
            switch(choice){
                case 0: exit();
                case 1: task1();
                    break;
                case 2: task2();
                    break;
                case 3: newSchedule();
                    break;
                case 4: showMenu();
                    break;
                default: System.out.println("Tokio pasirinkimo nėra!");
                    break;
            }
            System.out.print("\nĮveskite meniu pasirinkimą: \n");
            choice = input.nextInt();
            input.nextLine();
        }
    }

    //Simple menu of program functions
    private void showMenu(){
        System.out.println("\n**********************************************************************");
        System.out.println("*                     Paskolos grąžinimo grafikas                    *");
        System.out.println("**********************************************************************");
        System.out.println("*[1] 1-os užduoties failo paskolos grąžinimo grafiko generavimas     *");
        System.out.println("*[2] 2-os užduoties failo paskolos grąžinimo grafiko generavimas     *");
        System.out.println("*[3] Naujo grafiko generavimas                                       *");
        System.out.println("*[4] `Pakartoti meniu pasirinkimus                                   *");
        System.out.println("*[0] `Išeiti iš sistemos                                             *");
        System.out.println("**********************************************************************\n");
    }

    //First task: date = 2017-04-15, loanSize = 5000, numberOfIncomes = 24, interestRate = 12.
    //Date format 04/15/2017
    private void task1(){
        try {
            //Writing csv file with initialized parameters for ammortization schedule.
            FileWriter fileWriter = new FileWriter("task1.csv");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("\"Payment #\";\"Payment date\";\"Remaining amount\";\"principal payment\";" +
                    "\"Interest payment\";\"Total payment\";\"Interest rate\"");
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse("04/15/2017");
            BigDecimal loanSize = new BigDecimal("5000");
            AmortizationSchedule a = new AmortizationSchedule(date,12,loanSize,24,0);
            a.generateSchedule( printWriter);
            printWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Second task: date = 2017-04-15, loanSize = 5000, numberOfIncomes = 24, interestRate = 12 till 2017-09-01.
    //On 2017-09-02 interestRate = 9.
    private void task2(){
        try {
            //Writing to csv file.
            FileWriter fileWriter = new FileWriter("task2.csv");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("\"Payment #\";\"Payment date\";\"Remaining amount\";\"Principal payment\";" +
                    "\"Interest payment\";\"Total payment\";\"Interest rate\"");

            //Initializing schedule parameters.
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse("04/15/2017");
            BigDecimal loanSize = new BigDecimal("5000");
            AmortizationSchedule a = new AmortizationSchedule(date,12,loanSize,24,5);

            //Generating schedule including 5 months of payment.
            a.generateSchedule( printWriter);

            //Changing schedule parameters and generating remaining payments.
            a.setInterestRate(9);
            a.setMonths(0);
            a.setNumberOfIncomes(24-5);

            //Generating schedule.
            a.generateSchedule(printWriter);
            printWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Generating new schedule with user parameters.
    private void newSchedule(){
        try {
            AmortizationSchedule a = new AmortizationSchedule();

            //Setting date
            System.out.println("\nĮveskite skolos pirmos įmokos datą formatu MM/DD/YYYY:");
            String dateLine = input.nextLine();
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dateLine);
            a.setDate(date);

            //Setting number of incomes.
            System.out.println("\nĮveskite įmokų skaičių:");
            a.setNumberOfIncomes(input.nextInt());
            input.nextLine();

            //Setting loan size.
            System.out.println("\nĮveskite paskolos dydį:");
            BigDecimal loanSize = new BigDecimal(input.nextDouble());
            a.setLoanSize(loanSize);
            input.nextLine();

            //Setting interest rate.
            System.out.println("\nĮveskite metines palūkanas:");
            a.setInterestRate(input.nextDouble());
            input.nextLine();

            //Generating new csv file with ammortization schedule.
            FileWriter fileWriter = new FileWriter("newSchedule.csv");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("\"Payment #\";\"Payment date\";\"Remaining amount\";\"Principal payment\";" +
                    "\"Interest payment\";\"Total payment\";\"Interest rate\"");
            a.generateSchedule(printWriter);
            printWriter.close();
        } catch (ParseException e) {
            System.out.println("Neteisingai įvesta data!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println("Neteisinga įvestis!");
            input.next();
        }
    }

    //Exiting program.
    private void exit(){
        System.exit(1);
    }

}