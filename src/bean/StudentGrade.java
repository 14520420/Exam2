package bean;

public class StudentGrade {
    private String studentNo;
    private String studentName;
    private String subjectName;
    private int point;

    public StudentGrade() {}

    public StudentGrade(String studentNo, String studentName, String subjectName, int point) {
        this.studentNo = studentNo;
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.point = point;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
