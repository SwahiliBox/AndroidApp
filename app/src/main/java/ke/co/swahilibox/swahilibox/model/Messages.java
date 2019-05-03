package ke.co.swahilibox.swahilibox.model;

import com.google.gson.annotations.SerializedName;

public class Messages {

    @SerializedName("employee_salary")
    private String salary;

    @SerializedName("employee_name")
    private String name;

    public Messages(){}

    public Messages(String salary, String name) {
        this.salary = salary;
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
