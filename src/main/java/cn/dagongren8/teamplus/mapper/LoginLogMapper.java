package cn.dagongren8.teamplus.mapper;


import cn.dagongren8.teamplus.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Mapper
public interface LoginLogMapper {

    List<LoginLog> getAllUserLoginLog();

    LoginLog getLoginLogById(int logId);

    int insertLoginLog(LoginLog loginLog);

    int updateLoginLogById(LoginLog loginLog);

    int deleteLoginLogById(int logId);

}
