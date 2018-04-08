import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <p>Title: FieldsValidation</p> 
 * 
 * <p>Description: Java utility to validate email, phone number, ssn using regular expressions</p> 
 * 
 * <p>Copyright: Copyright (c) 2007</p> 
 * 
 * <p>Company: zParacha.com</p>
 * 
 * @author : zParacha
 * @version 1.0
 */
public class FieldsValidation{

/**
 * main method: Test client to demonstrate the use of utility class.
 * @param args[]. An array of String objects (email, phoneNumber, SSN, number).
 */
 public static void main(String args[]){
  String email = "zparacha@zparacha.com";
  String phoneNumber = "9729088589";
  String ssn = "789858569";
  String number = "1234";
  if(args.length > 2){
   email = args[0];
   phoneNumber = args[1];
   ssn = args[2];
   number = args[3];
   
  }
  if(!FieldsValidation.isEmailValid(email)){
   System.out.println(email + " is not a valid email address.");
  }else{
   System.out.println(email + " is a valid email address.");
  }
  if(!FieldsValidation.isPhoneNumberValid(phoneNumber)){
   System.out.println(phoneNumber + " is not a valid phone Number.");
  }else{
   System.out.println(phoneNumber + " is a valid phone number.");
  }
  if(!FieldsValidation.isSSNValid(ssn)){
   System.out.println(ssn + " is not a valid SSN.");
  }else{
   System.out.println(ssn + " is a valid SSN.");
  }
  if(!FieldsValidation.isNumeric(number)){
   System.out.println(number + " is a not valid number.");
  }else{
   System.out.println(number + " is a valid number."); 
  }
 }
 
 /**
 * This method  checks if the input email address is a valid email addrees.
 * @param email String. Email adress to validate
 * @return boolean: true if email address is valid, false otherwise.
 */
 public static boolean isEmailValid(String email){
  boolean isValid = false;
  /*
   Email format: A valid email address will have following format 
   [\\w\\.-]+ : Begins with word characters, (may include periods and hypens).
   @: It must have a '@' symbol after initial characters.
   ([\\w\\-]+\\.)+ : @ must follow by more alphanumeric characters (may include hypens.). This part must also have a "." to separate domain and subdomain names.
   [A-Z]{2,4}$: Must end with two to four alaphabets. (This will allow domain names with 2, 3 and 4 characters e.g pa, com, net, wxyz)
   */
 String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
 CharSequence inputStr = email;
 //Make the comparison case-insensitive. 
 Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
 Matcher matcher = pattern.matcher(inputStr);
 if(matcher.matches()){
  isValid = true;
 }
 return isValid;
 }
 
 /**
 * This method  checks if the input phone number is a valid phone number.
 * @param email String. Phone number to validate
 * @return boolean: true if phone number is valid, false otherwise.
 */
 public static boolean isPhoneNumberValid(String phoneNumber){
   boolean isValid = false;
   /* Phone Number format:
        ^\\(? : May start with an option "(" .
	(\\d{3}): Followed by 3 digits.
	\\)? : May have an optional ")"
	[- ]? : May have an optional "-" after the first 3 digits or after optional ) character.
	(\\d{3}) : Followed by 3 digits.
	[- ]? : May have another optional "-" after numeric digits.
	(\\d{4})$: ends with four digits.
	Matches following:
	 (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
	
   */
   String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
   CharSequence inputStr = phoneNumber;
   Pattern pattern = Pattern.compile(expression);
   Matcher matcher = pattern.matcher(inputStr);
   if(matcher.matches()){
   isValid = true;
   }
   return isValid; 
 }
 
 /**
 * This method  checks if the input social security number is valid.
 * @param email String. Social Security number to validate
 * @return boolean: true if social security number is valid, false otherwise.
 */
 public static boolean isSSNValid(String ssn){
   boolean isValid = false;
   //SSN format:
   /*
       ^\\d{3} : Starts with three numeric digits.
	[- ]?   : Followed by an optional - and space
	\\d{2}: Two numeric digits after the optional "-"
	[- ]? : May contains an optional second "-" character.
	\\d{4}:  ends with four numeric digits.
   */
   String expression = "^\\d{3}[- ]?\\d{2}[- ]?\\d{4}$";
   CharSequence inputStr = ssn;
   Pattern pattern = Pattern.compile(expression);
   Matcher matcher = pattern.matcher(inputStr);
   if(matcher.matches()){
   isValid = true;
   }
   return isValid; 
 }
 
  /**
 * This method  checks if the input text contains all numeric characters.
 * @param email String. Number to validate
 * @return boolean: true if the input is all numeric, false otherwise.
 */
 public static boolean isNumeric(String number){
   boolean isValid = false;
   //Number:
   /*
       ^\\d{3} : Starts with three numeric digits.
	[- ]?   : Followed by an optional - and space
	\\d{2}: Two numeric digits after the optional "-"
	[- ]? : May contains an optional second "-" character.
	\\d{4}:  ends with four numeric digits.
   */
   String expression = "[-+]?[0-9]*\\.?[0-9]+$";
   //expression = "/^(\(\\d+\) ?)?(\d+[\- ])*\d+$/";
   //expression = "^\\d{3}$";
   CharSequence inputStr = number;
   Pattern pattern = Pattern.compile(expression);
   Matcher matcher = pattern.matcher(inputStr);
   if(matcher.matches()){
   isValid = true;
   }
   return isValid; 
 }
}