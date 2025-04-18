package cn.cuit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 学生信息表
 * @TableName student
 */
@TableName(value ="student")
@Data
public class Student {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer studentId;

    /**
     * 
     */
    @TableField("student_name")
    private String studentName;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private Integer age;

    /**
     * 
     */
    private String phoneNumber;

    /**
     * 
     */
    private String photo;

    /**
     * 
     */
    private String gender;

    /**
     * 
     */
    private String idCard;

    /**
     * 
     */
    @TableField("last_modify_date")
    private Date lastModifyDate;

    /**
     * 
     */
    @TableField("registration_status")
    private String registrationStatus;

    /**
     入学年份
     */
    private Date enrollmentYear;

    /**
     专业
     */
    private String major;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Student other = (Student) that;
        return (this.getStudentId() == null ? other.getStudentId() == null : this.getStudentId().equals(other.getStudentId()))
            && (this.getStudentName() == null ? other.getStudentName() == null : this.getStudentName().equals(other.getStudentName()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()))
            && (this.getPhoneNumber() == null ? other.getPhoneNumber() == null : this.getPhoneNumber().equals(other.getPhoneNumber()))
            && (this.getPhoto() == null ? other.getPhoto() == null : this.getPhoto().equals(other.getPhoto()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getIdCard() == null ? other.getIdCard() == null : this.getIdCard().equals(other.getIdCard()))
            && (this.getLastModifyDate() == null ? other.getLastModifyDate() == null : this.getLastModifyDate().equals(other.getLastModifyDate()))
            && (this.getRegistrationStatus() == null ? other.getRegistrationStatus() == null : this.getRegistrationStatus().equals(other.getRegistrationStatus()))
            && (this.getEnrollmentYear() == null ? other.getEnrollmentYear() == null : this.getEnrollmentYear().equals(other.getEnrollmentYear()))
            && (this.getMajor() == null ? other.getMajor() == null : this.getMajor().equals(other.getMajor()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStudentId() == null) ? 0 : getStudentId().hashCode());
        result = prime * result + ((getStudentName() == null) ? 0 : getStudentName().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getAge() == null) ? 0 : getAge().hashCode());
        result = prime * result + ((getPhoneNumber() == null) ? 0 : getPhoneNumber().hashCode());
        result = prime * result + ((getPhoto() == null) ? 0 : getPhoto().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getIdCard() == null) ? 0 : getIdCard().hashCode());
        result = prime * result + ((getLastModifyDate() == null) ? 0 : getLastModifyDate().hashCode());
        result = prime * result + ((getRegistrationStatus() == null) ? 0 : getRegistrationStatus().hashCode());
        result = prime * result + ((getEnrollmentYear() == null) ? 0 : getEnrollmentYear().hashCode());
        result = prime * result + ((getMajor() == null) ? 0 : getMajor().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", studentId=").append(studentId);
        sb.append(", studentName=").append(studentName);
        sb.append(", name=").append(name);
        sb.append(", password=").append(password);
        sb.append(", age=").append(age);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", photo=").append(photo);
        sb.append(", gender=").append(gender);
        sb.append(", idCard=").append(idCard);
        sb.append(", lastModifyDate=").append(lastModifyDate);
        sb.append(", registrationStatus=").append(registrationStatus);
        sb.append(", enrollmentYear=").append(enrollmentYear);
        sb.append(", major=").append(major);
        sb.append("]");
        return sb.toString();
    }
}