
package Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.validator.EmailValidator;

public class Validation {
    
    /** Is that a correct email address? */
    public boolean emailValidation(String email){
        boolean valid = EmailValidator.getInstance().isValid(email);
        return valid;
    }
    
    /** Is the field empty? */
    public boolean isEmpty(String data){
        boolean valid = false;
        if(data==""){
            valid = true;
        }
        return valid;
    }
    
    /** Is the given number positive? */
    public boolean isPositive(String data){
        boolean valid = true;
        double d = Double.parseDouble(data);
        if(d>0){
            valid = false;
        }
        
        return valid;
    }
    
    /** Get the date of today! */
    public Date today() throws ParseException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date = dateFormat.parse(dateFormat.format(date));
        return date;
    }
    
    /** Set the correct date format! */
    public Date getDateFormat(String inputDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException ex) {
           System.out.println(ex);
        }
        return date;
    }
    
    /** Enums */
    private static enum enumItems {
        IN_STOCK,
        OUT_OF_STOCK;
    }
    
    /** Is this expression included in the enum? */
    public boolean enumTest(String inputData){
        boolean match = false;
        for (enumItems item : enumItems.values()) {
            if (item.name().equals(inputData)) {
                match = true;
            }
        }
        return match;
    }
    
    /** Is this data already included in the database? */
    public boolean matchingId(String fieldDBName, String fieldName) throws Exception{
        boolean id = false;
        int dataCounter = 0;
        Db db = new Db();
        Connection con = db.getConnection();
        PreparedStatement query = con.prepareStatement("SELECT * FROM order_item WHERE "+fieldDBName+" = '"+ fieldName +"'");
        ResultSet rs = query.executeQuery();
        while(rs.next()){
            dataCounter ++ ;
        }

        if(dataCounter>0){
            id = true;
        }
        rs.close();
        
        return id;
    }
}
