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
import model.PostModel;
import org.json.JSONObject;

/**
 *
 * @author Chrestella Stephanie
 */
@WebService(serviceName = "post")
public class Post {

	/**
	 * Web service operation
	 * @param title
	 * @param date
	 * @param content
	 * @return 
	 */
	@WebMethod(operationName = "create_post")
	public String create_post(@WebParam(name = "title") String title, @WebParam(name = "date") String date, @WebParam(name = "content") String content) {

		long created_at = System.currentTimeMillis() % 1000;
		long updated_at = System.currentTimeMillis() % 1000;
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");
		
		Map<String, Object> post = new HashMap<>();
		post.put("title", title);
		post.put("date", date);
		post.put("content", content);
		post.put("status", false);
		post.put("deleted_at", -1);
		post.put("created_at", created_at);
		post.put("updated_at", updated_at);
		postRef.push().setValue(post);
		
		String post_id = postRef.getKey();
		
		return post_id;
	}

	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getAllPosts")
	public List<PostModel> getAllPosts() {
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			p.setTitle(o.getString("title"));
			p.setDate(o.getLong("date"));
			p.setContent(o.getString("content"));
			p.setStatus(o.getBoolean("status"));
			posts.add(p);
		}

		return posts;
	}

	/**
	 * Web service operation
	 * @param id
	 * @param title
	 * @param status
	 * @param date
	 * @param content
	 */
	@WebMethod(operationName = "updatePost")
	public boolean updatePost(@WebParam(name = "id") String id, @WebParam(name = "title") String title, @WebParam(name = "date") long date, @WebParam(name = "content") String content, @WebParam(name = "status") boolean status) {

		long updated_at = System.currentTimeMillis() % 1000;

		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<>();
		post.put("title", title);
		post.put("date", date);
		post.put("content", content);
		post.put("status", status);
		post.put("updated_at", updated_at);
		postRef.updateChildren(post);
		return true;
	}

	
	/**
	 * Web service operation
	 * @param id
	 */
	@WebMethod(operationName = "deletePost")
	public boolean deletePost(@WebParam(name = "id") String id) {
		
		long deleted_at = System.currentTimeMillis() % 1000;
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<>();
		post.put("deleted_at", deleted_at);
		postRef.updateChildren(post);
		return true;
	}
	
	/**
	 * Web service operation
	 * @param id
	 */
	@WebMethod(operationName = "restorePost")
	public boolean restorePost(@WebParam(name = "id") String id) {
		
		long updated_at = System.currentTimeMillis() % 1000;

		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<>();
		post.put("deleted_at", -1);
		post.put("updated_at", updated_at);
		postRef.updateChildren(post);
		return true;
	}

	/**
	 * Web service operation
	 * @param id
	 * @return 
	 */
	@WebMethod(operationName = "getPost")
	public PostModel getPost(@WebParam(name = "id") String id) {
		//TODO write your implementation code here:
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);

		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);

		PostModel p = new PostModel();
		if (obj.getLong("deleted_at") > -1) {
			p.setId(id);
			p.setTitle(obj.getString("title"));
			p.setDate(obj.getLong("date"));
			p.setContent(obj.getString("content"));
			p.setStatus(obj.getBoolean("status"));
		}
		
		return p;
	}

	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getAllPublishedPosts")
	public List<PostModel> getAllPublishedPosts() {
		//TODO write your implementation code here:
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			p.setTitle(o.getString("title"));
			p.setDate(o.getLong("date"));
			p.setContent(o.getString("content"));
			p.setStatus(o.getBoolean("status"));
			if (p.isStatus()) {
				posts.add(p);
			}
		}

		return posts;
	}

	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getAllUnpublishedPosts")
	public List<PostModel> getAllUnpublishedPosts() {
		//TODO write your implementation code here:
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			p.setTitle(o.getString("title"));
			p.setDate(o.getLong("date"));
			p.setContent(o.getString("content"));
			p.setStatus(o.getBoolean("status"));
			if (!p.isStatus()) {
				posts.add(p);
			}
		}
		return posts;
	}

	/**
	 * Web service operation
	 * @param id
	 */
	@WebMethod(operationName = "publishPost")
	public void publishPost(@WebParam(name = "id") String id) {
		
		long updated_at = System.currentTimeMillis() % 1000;

		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<>();
		System.out.println(id);
		post.put("status", true);
		post.put("updated_at", updated_at);
		postRef.updateChildren(post);
	}

	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getAllDeletedPosts")
	public List<PostModel> getAllDeletedPosts() {
		//TODO write your implementation code here:
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			p.setTitle(o.getString("title"));
			p.setDate(o.getLong("date"));
			p.setContent(o.getString("content"));
			p.setStatus(o.getBoolean("status"));
			if(o.getLong("deleted_at") > -1) {
				posts.add(p);
			}
		}
		return posts;
	}

}
