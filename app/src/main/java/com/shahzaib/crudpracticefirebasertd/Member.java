package com.shahzaib.crudpracticefirebasertd;

public class Member {

    private String name;
    private long age;
    private double height;
    private boolean isMale;
    private String memberID;


//  public Member(){} // empty constructor for firebase, in this project we only practice
//  to CRUD String, Long, Double and boolean data and not the java object

    public Member(String name, long age, double height, boolean isMale)
    {
        this.name = name;
        this.age = age;
        this.height = height;
        this.isMale = isMale;
    }

    public Member(String memberID,String name, long age, double height, boolean isMale)
    {
        this.memberID = memberID;
        this.name = name;
        this.age = age;
        this.height = height;
        this.isMale = isMale;
    }


    public String getMemberID()
    {
        return memberID;
    }

    public String getName() {
        return name;
    }

    public long getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public boolean isMale() {
        return isMale;
    }

    @Override
    public String toString() {
        return "\nName: "+name +"\nAge: "+age +"\nHeight: "+height +"\nisMale: "+isMale;
    }
}
