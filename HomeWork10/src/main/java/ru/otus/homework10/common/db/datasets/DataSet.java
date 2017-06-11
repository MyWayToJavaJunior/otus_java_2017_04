package ru.otus.homework10.common.db.datasets;

import javax.persistence.*;

@MappedSuperclass
public class DataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
