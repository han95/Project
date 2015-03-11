/**
 * This class
 * 
 * @author CMPUT301W15T06
 * @version 03/07/2015
 * @see android.widget.EditText
 */
package ca.ualberta.CMPUT301W15T06;

import android.widget.EditText;

public class AppSingleton {
	private static AppSingleton appsingleton;
	private ClaimList claimList;
	private Claim currentClaim;
	private Item currentItem;
	private EditText dateEditText;
	private String editDate;
	private String status;

	
    private AppSingleton() {  
    	claimList=ClaimListManager.getInstance().load();
    }  
    
    public static AppSingleton getInstance() {  
        if (appsingleton == null) {  
        	appsingleton = new AppSingleton();  
        }  
        return appsingleton;  
    }
    

  	public ClaimList getClaimList(){
  		return claimList;
  	}

	public void setCurrentClaim(Claim claim) {
		currentClaim=claim;
		status=claim.getStatus();
	}

	public Claim getCurrentClaim() {
		// TODO Auto-generated method stub
		return currentClaim;
	}
	
	public void setCurrentItem(Item item) {
		currentItem=item;
	}
	
	public Item getCurrentItem() {
		return currentItem;	
	}


	
	public void setDateEditText(EditText date){
		dateEditText=date;
	}
	
	
	public EditText getDateEditText() {
		// TODO Auto-generated method stub
		return dateEditText;
	}
	
	public void setEditDate(String date){
		editDate=date;
	}
	
	public String getEditDate(){
		return editDate;
	}

	
	public String getStatus() {
		return status;

	}
	
	
	public static int getYear(String date){
		return Integer.valueOf(date.split("-")[0]);
	}
	
	public static int getMonth(String date){
		return Integer.valueOf(date.split("-")[1]);
	}
	
	public static int getDay(String date){
		return Integer.valueOf(date.split("-")[2]);
	}
    
}
