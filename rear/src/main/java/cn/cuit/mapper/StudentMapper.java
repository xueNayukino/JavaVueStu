package cn.cuit.mapper;


import cn.cuit.domain.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
/**
* @author wft
* @description 针对表【student(学生信息表)】的数据库操作Mapper
* @createDate 2025-01-13 10:58:43
* @Entity cn.cuit.domain.Student
*/
public interface StudentMapper extends BaseMapper<Student> {

    @Update("UPDATE student SET registration_status = '已报到', last_modify_date = #{date} " +
            "WHERE student_id = #{studentId} AND (registration_status IS NULL OR registration_status = '未报到')")
    boolean updateRegistrationStatus(@Param("studentId") Integer studentId, @Param("date") Date date);

}




