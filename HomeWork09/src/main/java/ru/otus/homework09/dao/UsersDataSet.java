package ru.otus.homework09.dao;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UsersDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id = null;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "name")
    private String name;

    public UsersDataSet() {
    }

    public UsersDataSet(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public UsersDataSet(Long id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UsersDataSet{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
