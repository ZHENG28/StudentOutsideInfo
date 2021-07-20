package com.zjut.student_outside.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Office
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String work;
    private String time;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
    private Student student;

    public Office()
    {
        super();
    }

    public Office(Long id, String work, String time, Student student)
    {
        super();
        this.id = id;
        this.work = work;
        this.time = time;
        this.student = student;
    }

    public Office(String work, String time, Student student)
    {
        super();
        this.work = work;
        this.time = time;
        this.student = student;
    }

    public Office(String work, String time)
    {
        super();
        this.work = work;
        this.time = time;
    }

    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    public String getWork()
    {
        return work;
    }


    public void setWork(String work)
    {
        this.work = work;
    }


    public String getTime()
    {
        return time;
    }


    public void setTime(String time)
    {
        this.time = time;
    }


    public Student getStudent()
    {
        return student;
    }


    public void setStudent(Student student)
    {
        this.student = student;
    }

    @Override
    public String toString()
    {
        return "Office{" +
                "id=" + id +
                ", work='" + work + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}