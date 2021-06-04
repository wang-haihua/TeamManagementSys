package cn.dagongren8.teamplus.mapper;


import cn.dagongren8.teamplus.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface UserMapper {

    List<User> getAllUsers();

    List<User> getAllTeamUsers(int teamId);

    User getUserById(int userId);

    User getUserByEmail(String userEmail);

    User getUserByTeamIdentifierAndEmail(@Param("teamIdentifier") String teamIdentifier, @Param("email") String email);

    int insertUser(User user);

    int updateUserById(User user);

    int deleteUserById(int userId);

}
