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
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import model.CommentModel;
import model.Database;
import org.json.JSONObject;

/**
 *
 * @author christangga
 */
@WebService(serviceName = "comment")
public class Comment {
	
	/**
	 * Web service operation
	 * @param post_id
	 * @param name
	 * @param email
	 * @param comments
	 * @return 
	 */
	@WebMethod(operationName = "create_comment")
	public String create_comment(@WebParam(name = "post_id") String post_id, @WebParam(name = "name") String name, @WebParam(name = "email") String email, @WebParam(name = "comment") String comments) {
		
		long created_at = System.currentTimeMillis() % 1000;
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("comments");
		
		Map<String, Object> c = new HashMap<>();
		c.put("post_id", post_id);
		c.put("name", name);
		c.put("email", email);
		c.put("comment", comments);
		c.put("created_at", String.valueOf(created_at));
		postRef.push().setValue(c);
		
		String comment_id = postRef.getKey();
		
		return comment_id;
	}

	/**
	 * Web service operation
	 */
	@WebMethod(operationName = "getAllComments")
	public List<CommentModel> getAllComments(@WebParam(name = "id") String id) {
		//TODO write your implementation code here:
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("comments");

		List<CommentModel> comments = new ArrayList<>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			CommentModel c = new CommentModel();
			c.setId(ids.next());
			JSONObject o = obj.getJSONObject(c.getId());
			
			c.setPost_id(o.getString("post_id"));
			c.setName(o.getString("name"));
			c.setEmail(o.getString("email"));
			c.setComment(o.getString("comment"));
			c.setCreated_at(o.getLong("created_at"));
			
			comments.add(c);
		}
		return comments;
	}




	
	

}
