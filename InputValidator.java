package it.alexpiex.mie;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alessandro Pietrucci on 16/02/15.
 * @version 2.0
 */
public class InputValidator extends DateUtil {

    public static final String NUMERIC = "[0-9]*";
    public static final String ALFANUMERIC="[A-Za-z0-9\\[\\]‡ËÏÚ˘¿»Ã“Ÿ·ÈÌÛ˙˝¡…Õ”⁄›‚ÍÓÙ˚¬ Œ‘€„Òı√—’‰ÎÔˆ¸ˇƒÀœ÷‹üÁ«ﬂÿ¯≈Â∆Êú\\.@:\\s'\\-/\\,<<>>_,*]*";
    public static final String DATE = "^(((0[1-9]|[12]\\d|3[01])\\/(0[13578]|1[02])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\/(0[13456789]|1[012])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\/02\\/((1[6-9]|[2-9]\\d)\\d{2}))|(29\\/02\\/((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$";
    public static final String BOOLEAN = "[0-1]";
    public static final String UPPERCASE = "\\p{Upper}*";
    public static final String COD_FISC = "[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]";
    public static final String IMPORT = "^([0-9]{1})+((\\,)[0-9]{1,2})?$";
    public static final String ATOM = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]";
    public static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
    public static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

    private static Pattern patternDate      = Pattern.compile("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}");
    private static Pattern patternDateTime  = Pattern.compile("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}");
    private static Pattern patternString    = Pattern.compile("[a-zA-Z0-9_.-]*");
    private static Pattern patternQualita   = Pattern.compile("[a-zA-Z0-9_.,-]*");
    private static Pattern patternNote      = Pattern.compile("[a-zA-Z0-9_.,\\s'-]*");
    private static Pattern patternPhone      = Pattern.compile("[0-9]{9,11}");
    private static Pattern patternEuro      = Pattern.compile("^(0|((\\d{1,3})(\\.\\d{3})*))(,\\d{1,2})?$");
    private static Pattern patternEuro2     = Pattern.compile("(^\\d+)(\\,\\d{1,2})?$");
    private static Pattern patternEmail     = Pattern.compile("^[0-9a-z]([-_.]?[0-9a-z])*@[0-9a-z]([-.]?[0-9a-z])*\\.([a-z]{2,4})$");
    private static Pattern patternEmail2    = Pattern.compile("[a-zA-Z0-9_.-]{3,50}@[a-zA-Z1-9-]{3,30}[.][a-z1-9]{2,5}([.][a-z1-9]{2,5})?");

    private static Pattern patternAlphabetic= Pattern.compile("([a-zA-Z‡-˘])+((\'(\\s)*){0,1}([a-zA-Z‡-˘])*)*");
    private static Pattern codiceFiscale    = Pattern.compile("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z]{1}[0-9]{2}[a-zA-Z]{1}[0-9]{3}[a-zA-Z]{1}");
    private static Pattern patternField     = Pattern.compile("[a-zA-Z‡-˘0-9.,'\\s]*");

    private final static String SPECIALCHARS = "/*!@#$%^&*Á∞()\"{}_[]|\\?<>,.'";
    private static final char [] SPECIAL_CHAR  = new char[]{'/','*','!','@','#','$','%','^','&','*','Á','∞','(',')','\"','{','}','_','[',']','|','\\','?','<','>',',','.','\'' } ;
    private static StringBuilder _errorMsg = new StringBuilder();
    private static Map<String, String> _error = new HashMap<String, String>();

    public static String getErrorMsg() {

        String strOut = "";
        if(!_error.isEmpty()){
            for (Map.Entry entry : _error.entrySet()) {
                //System.out.println("Key: " + entry.getKey() + " Object: " + entry.getValue());
                _errorMsg.append(entry.getValue());
                _errorMsg.append("#");
            }

            String[] lines = _errorMsg.toString().split("#");

            for(String s: lines){
                //System.out.println("Content = " + s);
                //System.out.println("Length = " + s.length());
                strOut += s + "<br />";
            }
        }
        return strOut;
    }

    public static boolean hasErrors(){
        //return (errorMsg.toString().length()>0)?true:false;
        if(_error.isEmpty()){
           return false;
        } else {
            return true;
        }
    }

    public static boolean isEmptyString(String s) {
        if (s == null || s.trim().equals("") || s.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if a specific input can be converted to a specific type.
     *
     * @param input The input to it.alexpiex.test. Accepts String, int, double or float.
     * @param type	Which type to it.alexpiex.test against. Accepts 'int','float' or 'double'.
     * @return Boolean	True if can be transformed to requested type. False otherwise.
     */
    private static Boolean isType(String testStr, TypeEnum type) {
        try {

            if (type.getType().equalsIgnoreCase("float")) {
                Float.parseFloat(testStr);
            } else if (type.getType().equalsIgnoreCase("int")) {
                Integer.parseInt(testStr);
            } else if (type.getType().equalsIgnoreCase("double")) {
                Double.parseDouble(testStr);
            }
            return true;

        } catch(Exception e) {
            return false;
        }

    }

    public static boolean check(String regex, String input) {
        if (Pattern.matches(regex, input)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  Counts the number of special characters in s.
     */
    private static int countSpecialCharacterCount(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }
        int theCount = 0;

        for (int i = 0; i < s.length(); i++) {
            //String car = s.substring(i, i+1);
            if (SPECIALCHARS.contains(s.substring(i, i+1))) {
                theCount++;
            }
        }
        return theCount;
    }

    public static boolean isNotNullString(String str){

        if(isEmptyString(str)){
            return false;
        }

        return true;
    }

    public static boolean isValid(String str, boolean controlSpecialCharacter){

        if(isEmptyString(str)){
            return false;
        }

        if(controlSpecialCharacter){
            if(countSpecialCharacterCount(str)>0){
                return false;
            }
        }

        return true;
    }

    public static boolean isValid(String str, int min, int max){

        if(isEmptyString(str)) return false;

        if ( min == 0) min++;

        if(str.length()>=min && str.length()<=max){
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValid(String str, int max){

        if(isEmptyString(str)) return false;

        if(str.length()<=max){
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValid(String str, int max, boolean ControlSpecialCharacter){

        if(isEmptyString(str)) return false;

        if(ControlSpecialCharacter){
            if(countSpecialCharacterCount(str)>0){
                return false;
            }
        }

        if(str.length()<=max){
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValid(String str, int min, int max, boolean ControlSpecialCharacter){

        if(isEmptyString(str)) return false;

        if(ControlSpecialCharacter){
            if(countSpecialCharacterCount(str)>0){
                return false;
            }
        }

        if ( min == 0) {
            min++;
        }

        if(str.length()>=min && str.length()<=max){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNum(String num){

        if(isEmptyString(num)) return false;
        //boolean b = num.matches("^[+-]?(?=.)\\d*(\\.\\d+)?$");
        boolean b = num.matches("-?\\d+");
        return b;
    }

    public static boolean isNumbers(String str) {

        if(isEmptyString(str)) return false;

        boolean esito = true;
        int countSeparator = 0;

        if (str == null || str.length() == 0) {
            esito = false;
        } else {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.' ){
                    if (i==0 && (str.charAt(i)=='-' || str.charAt(i)=='+')) {
                        continue;
                    }
                    esito = false;
                    break;
                }
                if (str.charAt(i) == '.' || str.charAt(i) == ',') {
                    countSeparator++;
                    if (countSeparator>1) {
                        esito = false;
                        break;
                    }
                }
            }
        }
        return esito;
    }

    public static boolean isEuro(String str) {
        if(isEmptyString(str)) return false;
        return patternEuro.matcher(str).matches();
    }

    public static boolean isEmail(String str) {
        if(isEmptyString(str)) return false;
        return patternEmail2.matcher(str).matches();
    }

    public static boolean isTelFax(String telFax){

        if(isEmptyString(telFax)) return false;

        if(telFax.length() != 0){
            for (int i = 0; i < telFax.length(); i++) {
                if (!Character.isDigit(telFax.charAt(i)) && telFax.charAt(i) != '.' && telFax.charAt(i) != ' '
                        && telFax.charAt(i) != '\\' && telFax.charAt(i) != '/' && telFax.charAt(i) != '-'){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isPhone(String phone){
        if(isEmptyString(phone)) return false;
        return patternPhone.matcher(phone).matches();
    }

    public static boolean isMobile(String mobile) {
        if(isEmptyString(mobile)) return false;
        return Pattern.matches("^[0-9]{10}$", mobile);
    }

    /**
     * Returns true if the specified number string represents a valid integer in
     * the specified range, using the default Locale.
     *
     * @param numberString a String representing an integer
     * @param min the minimal value in the valid range
     * @param max the maximal value in the valid range
     * @return true if valid, false otherwise
     */
    public static boolean isInteger(String numberString, int min, int max) {

        if(isEmptyString(numberString)) return false;

        DecimalFormat numberFormat = new DecimalFormat();
        Integer validInteger = null;
        try {
            Number aNumber = numberFormat.parse(numberString);
            int anInt = aNumber.intValue();
            if (anInt >= min && anInt <= max) {
                validInteger = new Integer(anInt);
            }
        } catch (ParseException e) {
            _error.put("isInteger(...)", e.getMessage());
        }
        return validInteger != null;
    }

    public static boolean isCodFisc(String codicefiscale) {

        if(isEmptyString(codicefiscale)){
            //appendError("La codice fiscale immesso vuoto!");
            _error.put("isCodFisc(...)", "Codice fiscale non valorizzato");
            return false;
        }

        Boolean b = false;
        try {
            //String codiceFiscaleReg = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z]{1}[0-9]{2}[a-zA-Z]{1}[0-9]{3}[a-zA-Z]{1}";
            b = codicefiscale.matches(codiceFiscale.pattern());
        } catch (Exception e) {
            //appendError("Error isValidCodFisc(): " + e.getMessage());
            _error.put("isCodFisc(...)", e.getMessage());
        }
        return b;
    }

    /**
     * Verifica che la stringa passata contega caratteti alfabetici
     *
     * @return
     * @param stringa
     */
    public static boolean isAlphabetic(String str) {
        if(isEmptyString(str)) return false;
        return patternAlphabetic.matcher(str).matches();
    }

    public static boolean isValidField(String str) {
        if(isEmptyString(str)) return false;
        return patternField.matcher(str).matches();
    }


    /**
     * Restituisce la stringa s trasformando le lettere accentate in non accentate,
     * ad esempio "andÚ" viene trasformata "ando".
     *
     * @param s
     * @return
     */
    public static String noStressedLetters(String s) {

        final String ACCENTATE = "¿»…Ã“Ÿ‡ËÈÏÚ˘";
        final String NOACCENTO = "AEEIOUaeeiou";
        int i = 0;
        //scorre la stringa originale
        while (i < s.length()) {
            int p = ACCENTATE.indexOf(s.charAt(i));
            //se ha trovato una lettera accentata
            if (p > -1) {
                //sostituisce con la relativa non accentata
                s = s.substring(0, i) + NOACCENTO.charAt(p) + s.substring(i + 1);
            }
            i++;
        }

        return s;
    }

     /**
      * Returns true if the specified string matches a string in the set of
      * provided valid strings, ignoring case if specified.
      *
      * @param value the String validate
      * @param validStrings an array of valid Strings
      * @param ignoreCase if true, case is ignored when comparing the value to
      * the set of validStrings
      * @return true if valid, false otherwise
      */
     public static boolean isRange(String value, String[] validStrings, boolean ignoreCase) {
         boolean isValid = false;
         for (int i = 0; validStrings != null && i < validStrings.length; i++) {
             if (ignoreCase) {
                 if (validStrings[i].equalsIgnoreCase(value)) {
                     isValid = true;
                     break;
                 }
             } else {
                 if (validStrings[i].equals(value)) {
                     isValid = true;
                     break;
                 }
             }
         }
         return isValid;
     }

    /**
     * Data una stringa con nome e cognome, lo trasforma con le iniziali in mauiscolo.
     * Esempio: maria adele mandela -> Maria Adele Mandela
     *          dell'orco adele     -> Dell'Orco Adele
     *
     * @param name
     * @return
     */
    public static String formatName(String name){

        if(isEmptyString(name)) return "";

        String ret= "";
        if (name != null && name.length() > 2) {
            name = name.replaceAll("\'", "\' ");
			//System.out.println("nuovo name: "+name);
            String[] nomi = name.split(" ");
            for (int i=0; i<nomi.length; i++) {
                String finalName = (""+nomi[i].charAt(0)).toUpperCase()+nomi[i].substring(1).toLowerCase();
                ret += finalName+" ";
                ret = ret.replaceAll("\' ", "\'");
            }
        }


        return ret.trim();
    }

    public static boolean isIban(String input) {

        if(isEmptyString(input)) return false;

        input.replaceAll(" ", "").trim();
        if (input.length() < 27) return false;

        if (!isOnlyString(input.substring(0, 2)))return false; // Posizioni 1 e 2 non possono contenere cifre

        String regex="[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}";

        if (!Pattern.matches(regex, input)) return false;

        return true;
    }

    public static boolean isOnlyString(String value){
        for (int k = 0; k< value.length(); k++){
            for (int j=0; j<SPECIAL_CHAR.length; j++){
                if(value.charAt(k) == SPECIAL_CHAR[j]){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isYear(String annoInput) {
        int annoI = -1;
        try {
            annoI = Integer.parseInt(annoInput);
        } catch (NumberFormatException ne) {
            _error.put("isYear(...)", ne.getMessage());
            return false;
        }
        //int anno = GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
        //Lanno di riferimento deve essere compreso tra 1900 e 4712
        if (annoI < 1900) {
            return false;
        } else if (annoI > 4712) {
            return false;
        }

        return true;
    }

    public static boolean isResource(String numeroRisorsa){

        if(isEmptyString(numeroRisorsa)) return false;

        String regex="[a-zA-Z0-9\\/\\.\\-\\_]{0,}";

        if (!Pattern.matches(regex, numeroRisorsa)){
            return false;
        }
        return true;

    }

    /**
     * Controlla se la stringa rappresenta importi validi con separatore virgola
     * @param importo
     * @return
     */
    public static boolean isImporto(String importo){
        String regex= "(^\\d+)(\\,\\d{1,2})?$";
        if (importo==null || !Pattern.matches(regex, importo))
            return false;
        return true;
    }

    /**
     * SubString con unica differenza il numero di caratteri da prelevare.
     *
     * @param str
     * @param start
     * @param numChar
     * @return
     */
    private static String sub(String str, int start, int numChar) {

        if(isEmptyString(str)) return "";

        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (start + numChar > str.length()) {
            throw new StringIndexOutOfBoundsException(start + numChar);
        }

        String stringa = str.substring(start, start + numChar);
        return stringa;

    }

    /**
     * Needs to have at least one digit
     * Needs to have at least one upper case letter
     * Needs to have at least one lower case letter
     * length of password should be at least 6
     * Needs to have at least one special character (%, $, #, @, !)
     * @param psw
     * @return
     */
    public static boolean validPassword (String psw) {

        if(isEmptyString(psw)) return false;

        int upperCaseCount = 0;
        int lowerCaseCount = 0;
        int digitCount = 0;
        int specialCharacterCount = 0;
        Boolean flag = false;

        int length = psw.length();

        if(length < 6) {
            //System.out.println("The password is invalid. Ensure you have a password with atleast 6 characters");
            _error.put("validPassword(...)", "The password is invalid. Ensure you have a password with atleast 6 characters");
            return false;
        }

        upperCaseCount = 0;
        lowerCaseCount = 0;
        digitCount = 0;
        specialCharacterCount = 0;

        for (int i = 0; i < length; i++) {
            char c = psw.charAt(i);

            if (Character.isUpperCase(c)) {
                upperCaseCount++;
            }

            if (Character.isLowerCase(c)) {
                lowerCaseCount++;
            }

            if (Character.isDigit(c)) {
                digitCount++;
            }

            if (c == '$' || c == '#' || c == '%' || c == '!' || c == '@') {
                specialCharacterCount++;
            }
        }

        if (upperCaseCount < 1) {
            //System.out.println("Invalid password. Add more uppercase characters");
            _error.put("validPassword(...)", "Invalid password. Add more uppercase characters");
            return false;
        }

        if (lowerCaseCount < 1) {
            //System.out.println("Invalid password. Add more lowercase characters");
            _error.put("validPassword(...)", "Invalid password. Add more lowercase characters");
            return false;
        }

        if (digitCount < 1) {
            //System.out.println("Invalid password. Add more digits");
            _error.put("validPassword(...)", "Invalid password. Add more digits");
            return false;
        }

        if (specialCharacterCount < 1) {
            //System.out.println("Invalid password. Add more special characters");
            _error.put("validPassword(...)", "Invalid password. Add more special characters");
            return false;
        }

        if (length >= 6 && upperCaseCount >= 1 && lowerCaseCount >= 1 && digitCount >= 1 && specialCharacterCount >= 1) {
            //System.out.println("The password has been successfully validated");
            _error.put("validPassword(...)", "The password has been successfully validated");
            return false;
        }


        return true;
    }

} //Fine



/**
 * Ogni enum ha una regex che definisce i numeri di una carta valida del suo genere.
 */
enum CreditCard {
    VISA("^4[0-9]{12}(?:[0-9]{3})?$"),
    MASTER_CARD("^5[1-5][0-9]{14}$"),
    AMERICAN_EXPRESS("^3[47][0-9]{13}$"),
    DINERS("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"),
    DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$"),
    JCB("^(?:2131|1800|35\\d{3})\\d{11}$");

    private String regex;

    CreditCard(String regex) {
        this.regex = regex;
    }

    public boolean matches(String card) {
        return card.matches(this.regex);
    }

    public static String gleanCompany(String card) {
        for (CreditCard cc : CreditCard.values()) {
            if (cc.matches(card)) {
                return String.valueOf(cc);
            }
        }
        return null;
    }
}

enum TypeEnum {

    FLOAT("float"), INT("int"), DOUBLE("double");

    private String tipo;

    private TypeEnum(String t) {
        tipo = t;
    }

    public String getType() {
        return tipo;
    }

}

enum ValidationStatus {
    EMPTYORNULL,
    INVALIDUSERNAME,
    VALIDUSERNAME,
    INVALIDPASSWORD,
    PASSWORDNOTMATCHING,
    VALIDPASSWORD,
    INVALIDDATE,
    VALIDDATE,
    INVALIDDOB,
    VALIDDOB,
    DOBTOOYOUNG,
    INVALIDNAME,
    VALIDNAME,
    INVALIDPHONENUMBER,
    VALIDPHONENUMBER,
    INVALID,
    VALID
}