package model;

// [OOP - Abstraction]
public abstract class TimeBlock {
    
    // [OOP - Encapsulation]
    protected String name;
    protected int duration; // dalam menit
    
    public TimeBlock(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    // Getter (Encapsulation)
    public String getName() { return name; }
    public int getDuration() { return duration; }

    // [OOP - Polymorphism & Abstraction]
    public abstract double calculateProductivityScore();
    
    // [OOP - Polymorphism]
    public abstract String getCategoryLabel();
}