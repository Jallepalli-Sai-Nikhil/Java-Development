package com.example.demo;


import jakarta.persistence.*;

@Entity
@Table(name="employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double salary;

    public Employee() {}

    public Employee(String name, Double salary) {
        this.name = name;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    };
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    };

}
