
package bean;

public class ClassGrade {
    private String classNum;
    private String subjectName;
    private double avgPoint;

    public ClassGrade() {}

    public ClassGrade(String classNum, String subjectName, double avgPoint) {
        this.classNum = classNum;
        this.subjectName = subjectName;
        this.avgPoint = avgPoint;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getAvgPoint() {
        return avgPoint;
    }

    public void setAvgPoint(double avgPoint) {
        this.avgPoint = avgPoint;
    }
}

