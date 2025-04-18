package cn.cuit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuit.domain.Student;
import cn.cuit.service.StudentService;
import cn.cuit.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author wft
* @description 针对表【student(学生信息表)】的数据库操作Service实现
* @createDate 2025-01-13 10:58:43
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentMapper studentMapper;
    
    @Override
    @Transactional
    public boolean updateById(Student student) {
        try {
            log.info("开始更新学生信息: {}", student);
            if (student.getLastModifyDate() == null) {
                student.setLastModifyDate(new Date());
            }
            boolean result = super.updateById(student);
            log.info("更新学生信息结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("更新学生信息失败: ", e);
            throw e;
        }
    }
}




