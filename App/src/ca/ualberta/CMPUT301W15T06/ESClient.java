package ca.ualberta.CMPUT301W15T06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

//https://github.com/joshua2ua/AndroidElasticSearch Author: Joshua Campbell
public class ESClient {
	private static final String USER_LIST = "http://cmput301.softwareprocess.es:8080/testing/usrlist/usrlist";

	private static final String USER = "http://cmput301.softwareprocess.es:8080/testing/usr/";

	// Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson;
	
	
	
	public ESClient(){
		gson=new GsonBuilder()
	    .registerTypeAdapter(Claim.class, new GsonAdapter<Claim>())
	    .registerTypeAdapter(Item.class, new GsonAdapter<Item>())
	    .registerTypeAdapter(Destination.class, new GsonAdapter<Destination>())
	    .registerTypeAdapter(Receipt.class, new GsonAdapter<Receipt>())
	    .create();
	}
	/**
	 * Consumes the POST/Insert operation of the service
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public void pushUser(User user) throws IllegalStateException, IOException {
		HttpPost httpPost = new HttpPost(USER+user.getUserName());
		StringEntity stringentity = null;
		try {
			stringentity = new StringEntity(gson.toJson(user));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		httpPost.setHeader("Accept","application/json");

		httpPost.setEntity(stringentity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}

		String status = response.getStatusLine().toString();
		Log.i("insert", status);
	}

	/**
	 * Consumes the Get operation of the service
	 * @return sr.getSource
	 */
	public User getUser(String string){
		Hit<User> sr = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(USER+string);

		HttpResponse response = null;

		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
			
		} catch (IOException e) {
			return null;
		}
		
		Type HitType = new TypeToken<Hit<User>>() {}.getType();

		try {
			sr = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()),
					HitType);
		} catch (JsonIOException e) {
			throw new RuntimeException(e);
		} catch (JsonSyntaxException e) {
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return sr.getSource();
	}

	public UserList getUserList() {
		// TODO Auto-generated method stub
		Hit<UserList> sr = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(USER_LIST);

		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			return null;
		}
		
		Type HitType = new TypeToken<Hit<UserList>>() {}.getType();

		try {
			sr = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()),
					HitType);
		} catch (JsonIOException e) {
			throw new RuntimeException(e);
		} catch (JsonSyntaxException e) {
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return sr.getSource();
	}

	public void synByUserList(UserList userList) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
		for (String name:userList.getUserList()){
			User user=ClaimListManager.getInstance().load(name);
			if(user.isNeedSyn()){
				pushUser(user);
				user.setNeedSyn(false);
			}
		}	
	}

	public void downloadUsers(UserList userList) {
		// TODO Auto-generated method stub
		ArrayList<User> ul=new ArrayList<User>();
		for (String name:userList.getUserList()){
			ul.add(getUser(name));
		}
		ClaimListManager.getInstance().saveUsers(ul);
	}

	public void pushUserList(UserList userList) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
		HttpPost httpPost = new HttpPost(USER_LIST);
		StringEntity stringentity = null;
		try {
			stringentity = new StringEntity(gson.toJson(userList));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		httpPost.setHeader("Accept","application/json");

		httpPost.setEntity(stringentity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		} 

		String status = response.getStatusLine().toString();
		Log.i("insert", status);
	}


}


