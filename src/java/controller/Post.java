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
	@WebMethod(operationName = "createPost")
	public String createPost(@WebParam(name = "title") String title, @WebParam(name = "date") long date, @WebParam(name = "content") String content) {

		long created_at = System.currentTimeMillis() % 1000;
		long updated_at = System.currentTimeMillis() % 1000;
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");
		
		Map<String, Object> post = new HashMap<String, Object>();
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

//	/**
//	 * Web service operation
//	 * @return 
//	 */
//	@WebMethod(operationName = "getAllPosts")
//	public List<PostModel> getAllPosts() {
//		
//		Database ref = Database.getDatabase();
//		Firebase postRef = ref.child("posts");
//
//		List<PostModel> posts = new ArrayList<PostModel>();
//		String json = Database.readURL(postRef.toString() + ".json");
//		JSONObject obj = new JSONObject(json);
//		
//		Iterator<String> ids = obj.keys();
//		while(ids.hasNext()) {
//			PostModel p = new PostModel();
//			p.setId(ids.next());
//			JSONObject o = obj.getJSONObject(p.getId());
//			p.setTitle(o.getString("title"));
//			p.setDate(o.getLong("date"));
//			p.setContent(o.getString("content"));
//			p.setStatus(o.getBoolean("status"));
//			posts.add(p);
//		}
//	
//		return posts;
//	}

	/**
	 * Web service operation
	 * @param id
	 * @param title
	 * @param status
	 * @param date
	 * @param content
	 * @return 
	 */
	@WebMethod(operationName = "updatePost")
	public boolean updatePost(@WebParam(name = "id") String id, @WebParam(name = "title") String title, @WebParam(name = "date") long date, @WebParam(name = "content") String content, @WebParam(name = "status") boolean status) {

		long updated_at = System.currentTimeMillis() % 1000;

		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<String, Object>();
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
	 * @return 
	 */
	@WebMethod(operationName = "deletePost")
	public boolean deletePost(@WebParam(name = "id") String id) {
		
		long deleted_at = System.currentTimeMillis() % 1000;
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<String, Object>();
		post.put("deleted_at", deleted_at);
		postRef.updateChildren(post);
		
		return true;
	}

	/**
	 * Web service operation
	 * @param id
	 * @return 
	 */
	@WebMethod(operationName = "restorePost")
	public boolean restorePost(@WebParam(name = "id") String id) {
		
		long updated_at = System.currentTimeMillis() % 1000;

		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<String, Object>();
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
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);

		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);

		PostModel p = new PostModel();
		p.setId(id);
		p.setTitle(obj.getString("title"));
		p.setDate(obj.getLong("date"));
		p.setContent(obj.getString("content"));
		p.setStatus(obj.getBoolean("status"));
		
		return p;
	}

	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getAllPublishedPosts")
	public List<PostModel> getAllPublishedPosts() {
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<PostModel>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			if (o.getLong("deleted_at") == -1) {
				if (o.getBoolean("status")) {
					p.setTitle(o.getString("title"));
					p.setDate(o.getLong("date"));
					p.setContent(o.getString("content"));
					p.setStatus(o.getBoolean("status"));
					
					posts.add(p);
				}
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
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<PostModel>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			if (o.getLong("deleted_at") == -1) {
				if (!o.getBoolean("status")) {
					p.setTitle(o.getString("title"));
					p.setDate(o.getLong("date"));
					p.setContent(o.getString("content"));
					p.setStatus(o.getBoolean("status"));
					
					posts.add(p);
				}
			}
		}
		
		return posts;
	}

	/**
	 * Web service operation
	 * @param id
	 * @return 
	 */
	@WebMethod(operationName = "publishPost")
	public boolean publishPost(@WebParam(name = "id") String id) {
		
		long updated_at = System.currentTimeMillis() % 1000;

		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts/" + id);
		
		Map<String, Object> post = new HashMap<String, Object>();
		post.put("status", true);
		post.put("updated_at", updated_at);
		postRef.updateChildren(post);
		
		return true;
	}

	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getAllDeletedPosts")
	public List<PostModel> getAllDeletedPosts() {
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<PostModel>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			if(o.getLong("deleted_at") > -1) {
				p.setTitle(o.getString("title"));
				p.setDate(o.getLong("date"));
				p.setContent(o.getString("content"));
				p.setStatus(o.getBoolean("status"));
				
				posts.add(p);
			}
		}
		
		return posts;
	}

	@WebMethod(operationName = "searchPost")
	public List<PostModel> searchPost(String key) {
		
		String[] keys = key.split(" ");
		
		Database ref = Database.getDatabase();
		Firebase postRef = ref.child("posts");

		List<PostModel> posts = new ArrayList<PostModel>();
		String json = Database.readURL(postRef.toString() + ".json");
		JSONObject obj = new JSONObject(json);
		
		Iterator<String> ids = obj.keys();
		while(ids.hasNext()) {
			PostModel p = new PostModel();
			p.setId(ids.next());
			JSONObject o = obj.getJSONObject(p.getId());
			if (o.getLong("deleted_at") == -1) {
				p.setTitle(o.getString("title"));
				p.setDate(o.getLong("date"));
				p.setContent(o.getString("content"));
				p.setStatus(o.getBoolean("status"));
				
				boolean isNotFound = false;
				for (String k : keys) {
					if (!(p.getTitle().contains(k) || p.getContent().contains(k))) {
						isNotFound = true;
						break;
					}
				}
				
				if (!isNotFound) {
					posts.add(p);
				}
			}
		}
		
		return posts;
	}
}
