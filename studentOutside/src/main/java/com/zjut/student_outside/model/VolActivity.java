package com.zjut.student_outside.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class VolActivity implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String time;
    private int manhour;

    @JsonBackReference
    @ManyToMany(mappedBy = "vols")
    private List<Student> students = new ArrayList<Student>();

    public VolActivity()
    {
        super();
    }

    public VolActivity(String name, String time)
    {
        super();
        this.name = name;
        this.time = time;
    }

    public VolActivity(Long id, String name, String time)
    {
        super();
        this.id = id;
        this.name = name;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    public int getManhour()
    {
        return manhour;
    }

    public void setManhour(int manhour)
    {
        this.manhour = manhour;
    }

    public List<Student> getStudents()
    {
        return students;
    }

    public void setStudents(List<Student> students)
    {
        this.students = students;
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
        VolActivity other = (VolActivity) obj;
        return Objects.equals(id, other.id);
    }


}
