/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class UserDTO {
    private final int id;
    private final String userName;
    private final String email;
    private final String role;

    //constructor

    public UserDTO(int id, String userName, String email, String role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    //
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
    //

    @Override
    public String toString() {
        return "UserDTO{" + "id=" + id + ", userName=" + userName + ", email=" + email + ", role=" + role + '}';
    }
    
}
