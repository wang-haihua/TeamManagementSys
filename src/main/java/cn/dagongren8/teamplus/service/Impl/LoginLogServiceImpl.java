package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.LoginLog;
import cn.dagongren8.teamplus.mapper.LoginLogMapper;
import cn.dagongren8.teamplus.service.LoginLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public class LoginLogServiceImpl implements LoginLogService {

    private final LoginLogMapper loginLogMapper;

    public LoginLogServiceImpl(LoginLogMapper loginLogMapper) {
        this.loginLogMapper = loginLogMapper;
    }

    @Override
    public List<LoginLog> getAllUserLoginLog() {
        return loginLogMapper.getAllUserLoginLog();
    }

    @Override
    public LoginLog getLoginLogById(int logId) {
        return loginLogMapper.getLoginLogById(logId);
    }

    @Override
    public int insertLoginLog(LoginLog loginLog) {
        return loginLogMapper.insertLoginLog(loginLog);
    }

    @Override
    public int updateLoginLogById(LoginLog loginLog) {
        return loginLogMapper.updateLoginLogById(loginLog);
    }

    @Override
    public int deleteLoginLogById(int logId) {
        return loginLogMapper.deleteLoginLogById(logId);
    }
}
