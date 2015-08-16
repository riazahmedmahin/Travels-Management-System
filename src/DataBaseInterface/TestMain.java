package DataBaseInterface;
import DataBaseInterface.Handler;
import DataBaseInterface.circle_type;

public class TestMain {

	private static Handler dbh;
	
	public static void main(String[] args) throws Exception{
		
		dbh = new Handler();
		Integer user_id = dbh.add_system_user("baskar","Baskar nallathambi","bas@gmail.com","12345","Admin");
		System.out.println("User Created with Id : "+user_id);
		
	    circle_type  trip_circle = 	circle_type.Outstation;
	    
	    Integer trip_slab_id = dbh.add_trip_slab(100,100,user_id,111);
	    System.out.println("Trip Slab Created with Id : "+trip_slab_id);
	  
	    //Integer trip_id = dbh.add_trip(111,123, trip_circle, 1.0, 1.0,100.0,0.0 ,0.0,0.0,0.0,0.0,0.0,0.0,100.0, "cocmments",user_id,1);
	     //System.out.println("Trip Created with Id : "+trip_id);
	    
	    Integer customer_id = dbh.add_customer("name","+911232","bas@bas.com","address 1",user_id,111);
	    System.out.println("Customer Created with Id : "+customer_id);
	    
	    Integer customer_vehicle_id = dbh.add_customer_vehicle(customer_id,"TN 47","TATA",user_id,111);
	    System.out.println("Customer vehicle Created with Id : "+customer_vehicle_id);
	    
	    Integer service_particulars_id = dbh.add_service_particulars("service_name",true,true,true,user_id,111);
	    System.out.println("SP Created with Id : "+ service_particulars_id);
	    
	    Integer vehicle_id = dbh.add_vehicle("TN 45","MARUTHI", user_id, 111);
	    System.out.println("Vehicle Created with Id : "+vehicle_id);
	    
	    Integer vehicle_service_id = dbh.add_vehicle_service(vehicle_id,service_particulars_id,1,100.0,111,true,"comments",user_id,111);
	    System.out.println("Vehicle Service Created with Id : "+vehicle_service_id);
	
	}	
}
