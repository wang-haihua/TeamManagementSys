package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.mapper.UserMapper;
import cn.dagongren8.teamplus.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public List<User> getAllTeamUsers(int teamId) {
        return userMapper.getAllTeamUsers(teamId);
    }

    @Override
    public PageInfo<User> getAllTeamUsers(int teamId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<User> users = userMapper.getAllTeamUsers(teamId);
        users.sort((a, b) -> {
            int[] map = new int[]{2, 0, 1};
            return map[a.getTeamAuthority()] - map[b.getTeamAuthority()];
        });
        return new PageInfo<>(users);
    }

    @Override
    public User getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userMapper.getUserByEmail(userEmail);
    }

    @Override
    public User getUserByTeamIdentifierAndEmail(String teamIdentifier, String email) {
        return userMapper.getUserByTeamIdentifierAndEmail(teamIdentifier, email);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public int updateUserById(User user) {
        return userMapper.updateUserById(user);
    }

    @Override
    public int deleteUserById(int userId) {
        return userMapper.deleteUserById(userId);
    }
}
