package blog.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import java.util.*;
/**
 * Created by user on 14.8.2017 г..
 */
@Entity
@Table(name="users")
public class User {
    private Integer id;
    private String email;
    private String fullName;
    @JsonBackReference
    private String password;
    @JsonBackReference
    private Set<Role> roles;
    @JsonBackReference
    private Set<Article> articles;
    @JsonBackReference
    private String picture;


    public User(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;

        this.roles=new HashSet<>();
        this.articles=new HashSet<>();
    }

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name="email",unique = true,nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name="fullName",nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @Column(name="password",length = 60,nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "author")
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles")
    public Set<Role> getRoles(){
        return roles;
    }
    public void setRoles(Set<Role> roles){
        this.roles=roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    @Transient
    public boolean isAdmin(){
        return this.roles.stream().anyMatch(role->role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Article article){
        return Objects.equals(this.getId(),article.getAuthor().getId());
    }

    @Transient
    public boolean isAuthorOfComment(Comment comment){ return Objects.equals(this.getFullName(),comment.getAuthorName()); }

    @Column(columnDefinition = "text")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
