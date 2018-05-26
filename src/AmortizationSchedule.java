import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AmortizationSchedule {
    private Date date;
    private double interestRate;
    private BigDecimal loanSize;
    private int numberOfIncomes;
    private int paymentNumber = 1;
    private int months;

    //Constructors

    //Default constructor
    AmortizationSchedule(){
    }

    AmortizationSchedule(Date date, double interestRate, BigDecimal loanSize, int numberOfIncomes, int months){
        this.date = date;
        this.interestRate = interestRate;
        this.loanSize = loanSize;
        this.numberOfIncomes = numberOfIncomes;
        this.months = months;
    }

    //Setters and getters
    public void setDate(Date date) {
        this.date = date;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setNumberOfIncomes(int numberOfIncomes) {
        this.numberOfIncomes = numberOfIncomes;
    }

    public void setLoanSize(BigDecimal loanSize) {
        this.loanSize = loanSize;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public Date getDate() {
        return date;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getNumberOfIncomes() {
        return numberOfIncomes;
    }

    public BigDecimal getLoanSize() {
        return loanSize;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public int getMonths() {
        return months;
    }

    //Generating amortization scheduleg
    public void generateSchedule(PrintWriter printWriter){
        //Calculating monthly interest rate from annual interest rate
        double monthlyRate = getInterestRate()/100/12;

        //Calculating total payment for loan per month
        double totalPayment = monthlyRate/(1-Math.pow(1+monthlyRate, -getNumberOfIncomes()))*getLoanSize().doubleValue();
        BigDecimal monthlyPayment = new BigDecimal(totalPayment);

        //Rounding monthly payment - two decimals after comma
        monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_UP);


        //Months - how many payments of loan we want to see on schedule (only first ten, all, etc.)
        //If number of payments to show is set to 0, showing all
        if(getMonths() == 0){
             setMonths(getNumberOfIncomes());
        }
        for(int i = 1; i<= getMonths(); i++){
            //Keeping correct format for date
            String date = new SimpleDateFormat("MM/dd/yyyy").format(getDate());

            //Monthly interest payment = principal balance (principal loan left to pay) x monthly interest rate
            BigDecimal interestPayment = new BigDecimal(monthlyRate * getLoanSize().doubleValue());
            interestPayment = interestPayment.setScale(2, RoundingMode.HALF_UP); // Rounding half up with two decimals after comma

            //Monthly principal payment = monthly payment - monthly interest payment
            BigDecimal principalPayment = monthlyPayment.subtract(interestPayment);
            principalPayment = principalPayment.setScale(2, RoundingMode.HALF_UP); // Rounding half up with two decimals after comma

            //Writing to csv file
            printWriter.print("\""+getPaymentNumber()+"\";\""+ date +"\";\""+getLoanSize()+"\";\""+principalPayment
                    +"\";\""+interestPayment+"\";\""+monthlyPayment+"\";\""+getInterestRate()+"\"\n");

            // Decreasing principal loan
            setLoanSize(getLoanSize().subtract(principalPayment));

            //Decreasing date by one month
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDate());
            cal.add(Calendar.MONTH, 1);
            setDate(cal.getTime());

            //Decreasing payment number
            setPaymentNumber(getPaymentNumber()+1);
        }
    }
}
