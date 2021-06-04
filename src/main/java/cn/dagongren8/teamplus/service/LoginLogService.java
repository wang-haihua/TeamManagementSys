package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.LoginLog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public interface LoginLogService {

    List<LoginLog> getAllUserLoginLog();

    LoginLog getLoginLogById(int logId);

    int insertLoginLog(LoginLog loginLog);

    int updateLoginLogById(LoginLog loginLog);

    int deleteLoginLogById(int logId);
}
