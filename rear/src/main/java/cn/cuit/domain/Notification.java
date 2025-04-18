package cn.cuit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 文件信息表
 * @TableName notification
 */
@TableName(value ="notification")
@Data
public class Notification {
    /**
     * 文件序号(主键,自增)
     */
    @TableId(type = IdType.AUTO)
    private Integer fileId;

    /**
     * 文件标题
     */
    private String fileTitle;

    /**
     * 文件内容
     */
    private String fileContent;

    /**
     * 发布人编号(外键,连接管理员编号)
     */
    private Integer publisherId;

    /**
     * 访问人数
     */
    private Integer viewCount;

    /**
     * 发布日期
     */
    private Date publishDate;

    /**
     * 修改日期
     */
    private Date modifyDate;

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
        Notification other = (Notification) that;
        return (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()))
            && (this.getFileTitle() == null ? other.getFileTitle() == null : this.getFileTitle().equals(other.getFileTitle()))
            && (this.getFileContent() == null ? other.getFileContent() == null : this.getFileContent().equals(other.getFileContent()))
            && (this.getPublisherId() == null ? other.getPublisherId() == null : this.getPublisherId().equals(other.getPublisherId()))
            && (this.getViewCount() == null ? other.getViewCount() == null : this.getViewCount().equals(other.getViewCount()))
            && (this.getPublishDate() == null ? other.getPublishDate() == null : this.getPublishDate().equals(other.getPublishDate()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getFileTitle() == null) ? 0 : getFileTitle().hashCode());
        result = prime * result + ((getFileContent() == null) ? 0 : getFileContent().hashCode());
        result = prime * result + ((getPublisherId() == null) ? 0 : getPublisherId().hashCode());
        result = prime * result + ((getViewCount() == null) ? 0 : getViewCount().hashCode());
        result = prime * result + ((getPublishDate() == null) ? 0 : getPublishDate().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileId=").append(fileId);
        sb.append(", fileTitle=").append(fileTitle);
        sb.append(", fileContent=").append(fileContent);
        sb.append(", publisherId=").append(publisherId);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", publishDate=").append(publishDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append("]");
        return sb.toString();
    }
}