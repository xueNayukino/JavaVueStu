package cn.cuit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuit.domain.Admin;
import cn.cuit.service.AdminService;
import cn.cuit.mapper.AdminMapper;
import org.springframework.stereotype.Service;

/**
* @author wft
* @description 针对表【admin】的数据库操作Service实现
* @createDate 2025-01-10 17:20:12
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService {

}




