public class Student {
    private StudentColor studentColor;

    public StudentColor getColor(){
        return studentColor;
    }

    public Student(StudentColor studentColor) {
        this.studentColor = studentColor;
    }
}
