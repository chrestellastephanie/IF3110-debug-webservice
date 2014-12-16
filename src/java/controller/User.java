/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import com.firebase.client.Firebase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import model.Database;
import model.UserModel;
import org.json.JSONObject;

/**
 *
 * @author christangga
 */
@WebService(serviceName = "user")
public class User {
	
	/**
	 * Web service operation
	 * @param email
	 * @param username
	 * @param password
	 * @param role
	 * @return 
	 */
	@WebMethod(operationName = "createUser")
	public boolean createUser(@WebParam(name = "email") String email, @WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "role") String role) {
		
		long created_at = System.currentTimeMillis();
		long updated_at = System.currentTimeMillis();
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("users");
		
		Map<String, Object> user = new HashMap<String,Object>();
		user.put("email", email);
		user.put("username", username);
		user.put("password", password);
		user.put("role", role);
		user.put("created_at", created_at);
		user.put("updated_at", updated_at);
		postRef.push().setValue(user);
		
		return true;
	}

	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getAllUsers")
	public List<UserModel> getAllUsers() {
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("users");

		List<UserModel> users = new ArrayList<>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			UserModel u = new UserModel();
			u.setId(ids.next());
			JSONObject o = obj.getJSONObject(u.getId());
			u.setEmail(o.getString("email"));
			u.setUsername(o.getString("username"));
			u.setPassword(o.getString("password"));
			u.setRole(o.getString("role"));
			users.add(u);
		}
		
		return users;
	}

	/**
	 * Web service operation
	 * @param id
	 * @return 
	 */
	@WebMethod(operationName = "getUser")
	public UserModel getUser(@WebParam(name = "id") String id) {
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("users/" + id);

		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);

		UserModel u = new UserModel();
		u.setEmail((String) obj.getString("email"));
		u.setUsername((String) obj.getString("username"));
		u.setPassword((String) obj.getString("password"));
		u.setRole((String) obj.getString("role"));
		return u;
	}

	/**
	 * Web service operation
	 * @param id
	 * @param email
	 * @param username
	 * @param password
	 * @param role
	 * @return 
	 */
	@WebMethod(operationName = "updateUser")
	public boolean updateUser(@WebParam(name = "id") String id, @WebParam(name = "email") String email, @WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "role") String role) {

		long updated_at = System.currentTimeMillis();

		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("users/" + id);
		
		Map<String, Object> user = new HashMap<String, Object>();
		user.put("email", email);
		user.put("username", username);
		user.put("password", password);
		user.put("role", role);
		user.put("updated_at", updated_at);
		postRef.updateChildren(user);
		return true;
	}

	/**
	 * Web service operation
	 * @param id
	 * @return 
	 */
	@WebMethod(operationName = "deleteUser")
	public boolean deleteUser(@WebParam(name = "id") String id) {
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("users/" + id);
		postRef.removeValue();
		return true;
	}
}
