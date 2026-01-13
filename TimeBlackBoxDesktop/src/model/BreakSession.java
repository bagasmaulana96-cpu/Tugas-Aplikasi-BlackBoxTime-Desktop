package model;

// [OOP - Inheritance]
public class BreakSession extends TimeBlock {

    public BreakSession(String name, int duration) {
        super(name, duration);
    }

    // [OOP - Polymorphism] 
    @Override
    public double calculateProductivityScore() {
        return 0.0;
    }

    // [OOP - Polymorphism] 
    @Override
    public String getCategoryLabel() {
        return "Istirahat";
    }
}