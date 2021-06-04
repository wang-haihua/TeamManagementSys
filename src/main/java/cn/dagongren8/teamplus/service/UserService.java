package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.User;
import com.github.pagehelper.PageInfo;
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
public interface UserService {

    List<User> getAllUsers();

    List<User> getAllTeamUsers(int teamId);

    PageInfo<User> getAllTeamUsers(int teamId, int pageIndex, int pageSize);

    User getUserById(int userId);

    User getUserByEmail(String userEmail);

    public User getUserByTeamIdentifierAndEmail(String teamIdentifier, String email);

    int insertUser(User user);

    int updateUserById(User user);

    int deleteUserById(int userId);
}
