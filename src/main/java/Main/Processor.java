
package Main;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Processor {
    
    public void manageDatas() throws IOException, Exception{
       
        try (
                    Reader reader = Files.newBufferedReader(Paths.get("src/files/inputFile.csv"));
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';')
                            .withFirstRecordAsHeader()
                            .withIgnoreHeaderCase()
                            .withTrim());
                ) {
                    List<ArrayList> output = new ArrayList<>();
                    
                    String vErrorText = "Errors: ";
                    for (CSVRecord csvRecord : csvParser) {

                        
                        ArrayList<String> outputComponents = new ArrayList<String>();
                        
                        boolean vError = false;
                        String vErrorMsg = "";
                        
                        //String lineNumber = csvRecord.get("LineNumber");
                        String orderItemId = csvRecord.get("OrderItemId");
                        String orderId = csvRecord.get("OrderId");
                        String buyerName = csvRecord.get("BuyerName");
                        String buyerEmail = csvRecord.get("BuyerEmail");
                        String address = csvRecord.get("Address");
                        String postcode = csvRecord.get("Postcode");
                        String salePrice = csvRecord.get("SalePrice");
                        String shippingPrice = csvRecord.get("ShippingPrice");
                        String sku = csvRecord.get("SKU");
                        String status = csvRecord.get("Status");
                        String orderDate = csvRecord.get("OrderDate");
                        
                        //VALIDATIONS
                        Validation validation = new Validation();

                        if(validation.isEmpty(orderItemId)){vError = true; vErrorMsg = vErrorMsg+"OrderItemId is missing, ";}
                        if(validation.matchingId("OrderItemId", orderItemId)){vError = true; vErrorMsg = vErrorMsg+"This orderItemId is already included in the database, ";}
                        Integer intOrderItemId = Integer.valueOf(orderItemId);
                        if(validation.matchingId("OrderId", orderId)){vError = true; vErrorMsg = vErrorMsg+"This orderId is already included in the database, ";}
                        if(validation.isEmpty(orderId)){vError = true; vErrorMsg = vErrorMsg+"OrderId is missing, ";}
                        Integer intOrderId = Integer.valueOf(orderId);
                        if(validation.isEmpty(buyerName)){vError = true; vErrorMsg = vErrorMsg+"BuyerName is missing, ";}
                        if(!validation.emailValidation(buyerEmail)){vError = true; vErrorMsg = vErrorMsg+"BuyerEmail is missing, ";}
                        if(validation.isEmpty(address)){vError = true; vErrorMsg = vErrorMsg+"Address is missing, ";}
                        if(validation.isEmpty(postcode)){vError = true; vErrorMsg = vErrorMsg+"Postcode is missing, ";}
                        Integer intPostcode = Integer.valueOf(postcode);
                        if(validation.isEmpty(salePrice)){vError = true; vErrorMsg = vErrorMsg+"SalePrice is missing, ";}
                        if(validation.isPositive(salePrice)){vError = true; vErrorMsg = vErrorMsg+"SalePrice is must be a positive number, ";}
                        double dSalePrice = Double.parseDouble(salePrice);
                        if(validation.isEmpty(shippingPrice)){vError = true; vErrorMsg = vErrorMsg+"ShippingPrice is missing, ";}
                        if(validation.isPositive(shippingPrice)){vError = true; vErrorMsg = vErrorMsg+"ShippingPrice is must be a positive number, ";}
                        double dShippingPrice = Double.parseDouble(shippingPrice);
                        if(dShippingPrice<1){
                            vErrorMsg = vErrorMsg+"ShippingPrice is less than 1.00, ";
                        }                       
                        
                        double orderTotalValue = dSalePrice + dShippingPrice; 
                        double totalItemPrice = dSalePrice + dShippingPrice;
                        if(validation.isEmpty(sku)){vError = true; vErrorMsg = vErrorMsg+"SKU is missing, ";}
                        if(validation.isEmpty(status)){vError = true; vErrorMsg = vErrorMsg+"Status is missing, ";}
                        if(!validation.enumTest(status)){vError = true; vErrorMsg = vErrorMsg+"The given status is incorrect, ";}
                        
                        
                        Date newOrderDate;
                        //Is the date fieled empty?
                        if(validation.isEmpty(orderDate)){
                            newOrderDate = validation.today();
                        }else{
                            newOrderDate = validation.getDateFormat(orderDate);
                        }
                        
                        System.out.println(newOrderDate);

                        
                        //Writting datas into database 
                       if(vError==false){     
                             try{
                                 Db db = new Db();
                                 Connection con = db.getConnection();                        
                                 PreparedStatement posted1 = con.prepareStatement("INSERT INTO orders(BuyerName, BuyerEmail, OrderDate, OrderTotalValue, Address, Postcode) VALUES('"+buyerName+"', '"+buyerEmail+"', '"+orderDate+"', '"+orderTotalValue+"', '"+address+"', '"+postcode+"' )");

                                 posted1.execute();
                                 posted1.close();
                             }catch(Exception e){
                                 System.out.println(e);
                                 vError = true; vErrorMsg = vErrorMsg+"Writing into the database was unsuccessful, ";
                             }finally{
                                 System.out.println("Insert completed");
                             }

                             try{
                                 Db db = new Db();
                                 Connection con = db.getConnection();                        
                                 PreparedStatement posted2 = con.prepareStatement("INSERT INTO order_item(OrderId, SalePrice, ShippingPrice, TotalItemPrice, Sku, Status) VALUES('"+orderId+"', '"+dSalePrice+"', '"+dShippingPrice+"', '"+totalItemPrice+"', '"+sku+"', '"+status+"' )");

                                 posted2.execute();
                                 posted2.close();
                             }catch(Exception e){
                                 System.out.println(e);
                                 vError = true; vErrorMsg = vErrorMsg+"Writing into the database was unsuccessful, ";
                             }finally{
                                 System.out.println("Insert completed");
                             }
                        }
                        
                        //Creating error message
                        if(vError){
                            vErrorText = vErrorText + " "+vErrorMsg;
                        }
                        
                        //Creating output information
                        long lineNumber = csvRecord.getRecordNumber();
                        String strLineNumber = ""+lineNumber;
                        outputComponents.add(strLineNumber);
                        if(vError == false){
                            outputComponents.add("OK");
                            outputComponents.add("");
                        }else{
                            outputComponents.add("ERROR");
                            outputComponents.add(vErrorText);
                        }
                        
                        output.add(outputComponents);
                       
            }
                    
            // Creating a response file
           OutputFileCreator ofc = new OutputFileCreator();
            ofc.createOutputFile(output, "src/files/responseFile.csv");
            Ftp uf = new Ftp();
            uf.uploadFile();

            for(int i=0; i<output.size(); i++){
                System.out.println(output.get(i));
            }
        }
    }
}
