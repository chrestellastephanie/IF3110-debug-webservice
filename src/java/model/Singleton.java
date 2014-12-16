/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author christangga
 */
public class Singleton {
	
	public Singleton instance;
	public UserModel active_user;
	public PostModel active_post;
	public CommentModel active_comment;
	
	private Singleton() {
	}
	
}
