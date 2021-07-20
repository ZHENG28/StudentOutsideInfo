package com.zjut.student_outside.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Student
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String banji;
    private String name;
    private String grade;

    @JsonBackReference(value = "officeSet")
    @OneToMany(mappedBy = "student", cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Office> offices = new ArrayList<Office>();


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "t_stu_vol",
            joinColumns = @JoinColumn(name = "stu_id"),
            inverseJoinColumns = @JoinColumn(name = "vol_id")
    )
    private List<VolActivity> vols = new ArrayList<VolActivity>();


    public Student()
    {
        super();
    }

    public Student(String banji, String name, String grade)
    {
        super();
        this.banji = banji;
        this.name = name;
        this.grade = grade;
    }


    public Student(Long id, String banji, String name, String grade)
    {
        super();
        this.id = id;
        this.banji = banji;
        this.name = name;
        this.grade = grade;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getBanji()
    {
        return banji;
    }

    public void setBanji(String banji)
    {
        this.banji = banji;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    public List<Office> getOffices()
    {
        return offices;
    }

    public void setOffices(List<Office> offices)
    {
        this.offices = offices;
    }

    public List<VolActivity> getVols()
    {
        return vols;
    }

    public void setVols(List<VolActivity> vols)
    {
        this.vols = vols;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString()
    {
        return "Student{" +
                "id=" + id +
                ", banji='" + banji + '\'' +
                ", name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
