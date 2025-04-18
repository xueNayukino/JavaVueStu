package cn.cuit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="log")
@Data
public class Log {
    /**
     * 日志序号(主键,自增)
     */
    @TableId(type = IdType.AUTO)
    private Integer modifyId;

    /**
     * 修改操作类型
     */
    @TableField("modify_operation_type")
    private String modifyOperationType;

    /**
     * 修改类容
     */
    @TableField("modify_content")
    private String modifyContent;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Date modifyTime;

    /**
     * 修改人id(外键,连接管理员id)
     */
    @TableField("admin_id")
    private Integer adminId;

    /**
     * 被修改人id(外键,连接学生id)
     */
    @TableField("student_id")
    private Integer studentId;

    /**
     * 被修改文件id(外键,连接文件序号)
     */
    @TableField("file_id")
    private Integer fileId;

    // 操作类型常量
    public static final String OPERATION_QUERY = "查询";
    public static final String OPERATION_MODIFY = "修改";
    public static final String OPERATION_PUBLISH = "发布";
    public static final String OPERATION_UNPUBLISH = "下架";

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
        Log other = (Log) that;
        return (this.getModifyId() == null ? other.getModifyId() == null : this.getModifyId().equals(other.getModifyId()))
                && (this.getModifyOperationType() == null ? other.getModifyOperationType() == null : this.getModifyOperationType().equals(other.getModifyOperationType()))
                && (this.getModifyContent() == null ? other.getModifyContent() == null : this.getModifyContent().equals(other.getModifyContent()))
                && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
                && (this.getAdminId() == null ? other.getAdminId() == null : this.getAdminId().equals(other.getAdminId()))
                && (this.getStudentId() == null ? other.getStudentId() == null : this.getStudentId().equals(other.getStudentId()))
                && (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getModifyId() == null) ? 0 : getModifyId().hashCode());
        result = prime * result + ((getModifyOperationType() == null) ? 0 : getModifyOperationType().hashCode());
        result = prime * result + ((getModifyContent() == null) ? 0 : getModifyContent().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        result = prime * result + ((getStudentId() == null) ? 0 : getStudentId().hashCode());
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modifyId=").append(modifyId);
        sb.append(", modifyOperationType=").append(modifyOperationType);
        sb.append(", modifyContent=").append(modifyContent);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", adminId=").append(adminId);
        sb.append(", studentId=").append(studentId);
        sb.append(", fileId=").append(fileId);
        sb.append("]");
        return sb.toString();
    }
}