package me.sativus.testplugin.DAO;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class Job {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    Boolean enabled;

    @OneToMany(mappedBy = "job", orphanRemoval = true, cascade = jakarta.persistence.CascadeType.ALL)
    List<Salary> salaries;

    // utils
    public void addSalary(Salary salary) {
        salary.setJob(this);
        this.salaries.add(salary);
    }

    public void removeSalary(Salary salary) {
        salary.setJob(null);
        this.salaries.remove(salary);
    }

    // constructors
    public Job() {
    }

    public Job(String name) {
        this.name = name;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSalaries(List<Salary> salaries) {
        this.salaries = salaries;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Long getId() {
        return this.id;
    }

    public List<Salary> getSalaries() {
        return this.salaries;
    }

}
